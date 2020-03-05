package net.atlassian.cmathtutor.adaptive.domain.data;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@NoArgsConstructor
public class QuestionDefinitionRuleData {

    @Schema(nullable = false)
    private Long id;

    @Schema(nullable = false)
    private Integer questionNumberFrom;

    private Integer questionNumberTo;

    @Schema(nullable = false)
    private Map<String, MinMarkRequirementData> minGradeMarkRequirements;

    @Schema(nullable = false)
    private List<QuestionData> questions;
}
