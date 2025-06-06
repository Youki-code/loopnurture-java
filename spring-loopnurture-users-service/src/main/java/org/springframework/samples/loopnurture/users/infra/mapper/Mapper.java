package org.springframework.samples.loopnurture.users.web.mapper;

public interface Mapper<R, E> {
    E map(E response, R request);
}
