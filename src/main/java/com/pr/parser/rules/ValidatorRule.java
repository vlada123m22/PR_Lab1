package com.pr.parser.rules;

public interface ValidatorRule<T> {
    T validate(T value);
}
