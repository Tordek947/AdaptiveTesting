package net.atlassian.cmathtutor.adaptive.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Points out that some unexpected server error occurred and request made no impact to server state.
 * This exception <b>must</b> have <code>Throwable</code> cause.
 * @author Hryhorii_Popov
 *
 */
@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }
}