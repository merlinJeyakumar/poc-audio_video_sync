package com.domain;

import retrofit2.HttpException;

import java.io.IOException;

public interface IAPIExceptionMapper {
    Throwable decodeHttpException(HttpException exception);

    Throwable decodeUnexpectedException(Throwable throwable);

    Throwable decodeIOException(IOException ioException);
}
