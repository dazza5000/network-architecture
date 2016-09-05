package com.amicly.bignerdranchadvanced.exception;

import java.io.IOException;

/**
 * Created by daz on 9/4/16.
 */

public class UnauthorizedException extends IOException {

    public UnauthorizedException(IOException cause) {
        super(cause);
    }
}