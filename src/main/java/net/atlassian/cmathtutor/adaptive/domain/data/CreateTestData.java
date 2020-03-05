package net.atlassian.cmathtutor.adaptive.domain.data;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateTestData {

    @Schema(nullable = false)
    private String name;

    @Schema(nullable = false)
    private String creatorName;

    @Schema(nullable = false)
    private List<CreateQuestionData> questions;

    @Schema(nullable = false)
    private Map<String, GradeParametersData> grades;
}
