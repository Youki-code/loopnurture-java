#!/bin/bash
docker tag ${REPOSITORY_PREFIX}/spring-loopnurture-config-server ${REPOSITORY_PREFIX}/spring-loopnurture-config-server:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-loopnurture-discovery-server ${REPOSITORY_PREFIX}/spring-loopnurture-discovery-server:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-loopnurture-api-gateway ${REPOSITORY_PREFIX}/spring-loopnurture-api-gateway:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-loopnurture-users-service ${REPOSITORY_PREFIX}/spring-loopnurture-users-service:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-loopnurture-mail-service ${REPOSITORY_PREFIX}/spring-loopnurture-mail-service:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-loopnurture-admin-server ${REPOSITORY_PREFIX}/spring-loopnurture-admin-server:${VERSION}
