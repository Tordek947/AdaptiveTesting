package net.atlassian.cmathtutor.adaptive.domain.data.csv;

import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class TextToScanGradeMinMarkRequirementDataCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
	String[] strings = value.split(":");
	String gradeCode = strings[0];
	Integer minMark;
	try {
	    minMark = Integer.valueOf(strings[1]);
	} catch (NumberFormatException e) {
	    throw new CsvDataTypeMismatchException(e.getMessage());
	}
	return ScanGradeMinMarkRequirementData.builder().gradeCode(gradeCode).minMark(minMark).build();
    }

}