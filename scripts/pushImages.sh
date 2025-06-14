#!/bin/bash
docker push ${REPOSITORY_PREFIX}/spring-loopnurture-config-server:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-loopnurture-discovery-server:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-loopnurture-api-gateway:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-loopnurture-users-service:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-loopnurture-mail-service:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-loopnurture-admin-server:${VERSION}
