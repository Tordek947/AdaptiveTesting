package net.atlassian.cmathtutor.adaptive.domain.data.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScanGradeMarkChangeRuleData {

    @CsvBindByName
    private Integer markDelta;

    @CsvBindByName
    private String grade;

    @CsvBindByName
    private Long questionAnswerId;
}
