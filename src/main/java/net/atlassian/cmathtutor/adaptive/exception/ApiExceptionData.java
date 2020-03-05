package net.atlassian.cmathtutor.adaptive.exception;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ApiExceptionData {

    private static final String DEFAULT_ERROR_MESSAGE = "Unexpected error";

    @JsonFormat(shape = JsonFormat.Shape.NATURAL)
    @Schema(required = true)
    private HttpStatus status;

    @Schema(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Builder.Default
    private final Date date = Calendar.getInstance().getTime();

    @Schema(required = true)
    @Builder.Default
    private String message = DEFAULT_ERROR_MESSAGE;

    @Schema(required = true)
    private String debugMessage;

    @Schema(required = true)
    private List<ApiSubExceptionData> subExceptions;
}
