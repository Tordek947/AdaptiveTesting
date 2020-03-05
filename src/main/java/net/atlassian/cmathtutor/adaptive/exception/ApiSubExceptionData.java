package net.atlassian.cmathtutor.adaptive.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiSubExceptionData {

    @Schema(required = true)
    private String exceptionName;

    @Schema(required = true)
    private String message;

    public ApiSubExceptionData(Throwable ex) {
	exceptionName = ex.getClass().getSimpleName();
	message = ex.getMessage();
    }
}