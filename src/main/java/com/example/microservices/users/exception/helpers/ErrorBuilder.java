package com.example.microservices.users.exception.helpers;

import com.example.microservices.users.entity.Error;
import com.example.microservices.users.entity.ErrorId;
import com.example.microservices.users.entity.ErrorMeta;
import com.example.microservices.users.repository.PropertiesBasedErrorRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * A guy who gets the data from the repo and transforms into Error domains
 */
@Getter
@Setter
public class ErrorBuilder {

    @Value("${errors.default.title:Ошибка обработки данных}")
    private String defaultTitle;

    @Value("${errors.default.statusCode:500}")
    private int defaultStatusCode;

    @Value("${errors.metaEnabled:true}")
    private boolean metaEnabled;

    @Autowired(required = false)
    private final List<PropertiesBasedErrorRepository> errorRepositories = new ArrayList<>();

    /**
     * Produces common error response builder
     * <p>
     * Fill `detail` field for human text and `meta` for tech message.
     * <p>
     * If field `title` is omitted it will try to fetch title from some repository
     * or use the {@link #defaultTitle}
     */
    public ErrorPrototype forErrorId(ErrorId errorId) {
        return new ErrorPrototype(errorId);
    }

    @Data
    @Accessors(chain = true, fluent = true)
    public class ErrorPrototype {
        private final ErrorId id;

        private Integer httpStatus;
        private String title;
        private String detail;
        private ErrorMeta meta;

        public Error build() {
            return new Error()
                    .setHttpStatus(isNull(httpStatus) ? defaultStatusCode : httpStatus)
                    .setFrontendCode(id.getOuterCode())
                    .setDetail(detail)
                    .setTitle(isBlank(title) ? determineTitle(id.getInnerCode(), defaultTitle) : title);
        }

        public ErrorPrototype httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus.value();
            return this;
        }

        /**
         * Fetches error title from some repository (possibly external service)
         * by {@param innerCode}.
         * <p>
         * If absent - use {@link #defaultTitle}
         */
        private String determineTitle(String innerCode, String defaultTitle) {
            if (isBlank(id.getInnerCode())) {
                return defaultTitle;
            }
            return errorRepositories.stream()
                    .map(repo -> repo.getErrorMessage(innerCode))
                    .filter(title -> !isBlank(title))
                    .findFirst()
                    .orElse(defaultTitle);
        }
    }
}
