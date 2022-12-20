package com.i3e3.mindlet.domain.admin.exception;

import com.i3e3.mindlet.global.exception.DuplicateException;

public class DuplicateIdException extends DuplicateException {

    public DuplicateIdException(String message) {
        super(message);
    }
}
