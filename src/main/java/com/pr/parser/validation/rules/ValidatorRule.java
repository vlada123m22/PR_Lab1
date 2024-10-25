package com.pr.parser.validation.rules;

public interface ValidatorRule<T> {

    T validate(T value);
}
