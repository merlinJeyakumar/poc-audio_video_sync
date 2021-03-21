package com.domain.usecases

abstract class UseCase<INPUT_TYPE, OUTPUT_TYPE> {
    abstract fun execute(params: INPUT_TYPE? = null): OUTPUT_TYPE
}