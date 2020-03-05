package net.atlassian.cmathtutor.adaptive.domain.data.csv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanGradeMinMarkRequirementData {

    private String gradeCode;

    private Integer minMark;
}
