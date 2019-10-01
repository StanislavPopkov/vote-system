package ru.vote.projectUtils.exception;

import org.springframework.http.HttpStatus;

public class TimeRestrictionException  extends ApplicationException {
    public static final String EXCEPTION_TIME_RESTRICTION = "exception.user.modificationRestriction";

    public TimeRestrictionException() {
        super(EXCEPTION_TIME_RESTRICTION , HttpStatus.NOT_ACCEPTABLE);
    }
}
