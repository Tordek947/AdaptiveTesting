package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import net.atlassian.cmathtutor.adaptive.domain.data.GradeData;
import net.atlassian.cmathtutor.adaptive.domain.data.GradeParametersData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
public class GradeMapper
	implements Mapper<Map.Entry<String, GradeParametersData>, GradeData, Map.Entry<String, Grade>> {

    @Override
    public Map.Entry<String, Grade> dataToEntity(Entry<String, GradeParametersData> data) {
	return new ImmutablePair<>(data.getKey(), Grade.builder()
		.code(data.getKey())
		.initialMark(data.getValue().getInitialMark())
		.maxMark(data.getValue().getMaxMark())
		.build());
    }

    @Override
    public GradeData entityToData(Map.Entry<String, Grade> entity) {
	Grade grade = entity.getValue();
	return GradeData.builder()
		.code(grade.getCode())
		.id(grade.getId())
		.initialMark(grade.getInitialMark())
		.maxMark(grade.getMaxMark())
		.build();
    }

}
