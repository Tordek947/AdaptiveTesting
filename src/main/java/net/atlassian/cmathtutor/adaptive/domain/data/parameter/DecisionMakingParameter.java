package net.atlassian.cmathtutor.adaptive.domain.data.parameter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionDefinitionRuleData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class DecisionMakingParameter {

    @Schema(nullable = false)
    private List<QuestionData> availableQuestions;
    @Schema(nullable = false)
    private List<QuestionDefinitionRuleData> appliedQuestionDefinitionRules;

}
