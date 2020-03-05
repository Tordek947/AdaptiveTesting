package net.atlassian.cmathtutor.adaptive.domain.data;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CreateMarkChangeRuleData {

    @Schema(nullable = false)
    private Integer markDelta;
}
