package net.atlassian.cmathtutor.adaptive.exception;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message, Throwable cause) {
	super(message, cause);
    }

    public NotFoundException() {
    }
}
