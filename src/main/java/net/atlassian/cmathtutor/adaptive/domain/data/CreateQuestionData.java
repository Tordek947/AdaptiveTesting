package net.atlassian.cmathtutor.adaptive.domain.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateQuestionData {

    @Schema(nullable = false)
    private String sentence;

    @Schema(nullable = false)
    private List<CreateQuestionAnswerData> questionAnwsers;
}
