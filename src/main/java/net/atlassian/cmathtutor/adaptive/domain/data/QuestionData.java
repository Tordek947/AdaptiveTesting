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
public class QuestionData {

    @Schema(nullable = false)
    private Integer id;

    @Schema(nullable = false)
    private String sentence;

    private List<QuestionAnswerData> questionAnwsers;

}
