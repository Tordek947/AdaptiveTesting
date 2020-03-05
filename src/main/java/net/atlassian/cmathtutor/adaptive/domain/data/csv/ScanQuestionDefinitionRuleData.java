package net.atlassian.cmathtutor.adaptive.domain.data.csv;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanQuestionDefinitionRuleData {

    @CsvBindByName
    private Integer questionNumberFrom;

    @CsvBindByName
    private Integer questionNumberTo;

    @CsvBindAndSplitByName(elementType = ScanGradeMinMarkRequirementData.class, splitOn = ",+",
	    converter = TextToScanGradeMinMarkRequirementDataCsvConverter.class)
    private Set<ScanGradeMinMarkRequirementData> minGradeMarkRequirements;

    @CsvBindAndSplitByName(elementType = Integer.class, collectionType = LinkedList.class, splitOn = ",+")
    private List<Integer> questionIds;
}
