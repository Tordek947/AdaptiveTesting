package net.atlassian.cmathtutor.adaptive.domain.data.parameter;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class TestStateParameter {

    @Schema(nullable = false)
    private int testId;
    @Schema(nullable = false)
    private int currentQuestionNumber;
    @Schema(nullable = false)
    private Map<String, Integer> gradeMarks;
    @Schema(nullable = false)
    private Set<Integer> alreadyDisplayedQuestionIds;

}
