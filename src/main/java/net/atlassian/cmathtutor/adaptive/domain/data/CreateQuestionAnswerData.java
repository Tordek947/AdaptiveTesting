package net.atlassian.cmathtutor.adaptive.domain.data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateQuestionAnswerData {

    @Schema(nullable = false)
    private String sentence;

    @Builder.Default
    private Boolean isCorrect = false;

    @Schema(nullable = false)
    private Map<String, CreateMarkChangeRuleData> gradeMarkChangeRules;
}
