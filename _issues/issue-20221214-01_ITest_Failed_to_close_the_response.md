# issue-20221214-01_ITest_Failed_to_close_the_response.md

Integration tests pass correctly but with an uncritical exception 
(see devops/docker/logs/testcontainers-with-docker.md)

## Debug notice
14:19:13.684 [docker-java-stream-451592005] DEBUG com.github.dockerjava.zerodep.ApacheDockerHttpClientImpl$ApacheResponse - Failed to close the response
java.io.IOException: java.nio.channels.ClosedChannelException