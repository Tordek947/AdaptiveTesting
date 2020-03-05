package net.atlassian.cmathtutor.adaptive.domain.data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class TestData {

    @Schema(nullable = false)
    private Integer id;

    @Schema(nullable = false)
    private String name;

    @Schema(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;

    private String creatorName;

    @Schema(nullable = false)
    private List<QuestionData> questions;

    @Schema(nullable = false)
    private List<GradeData> grades;
}
