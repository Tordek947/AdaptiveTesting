package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import net.atlassian.cmathtutor.adaptive.domain.data.MinMarkRequirementData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.MinGradeMarkRequirement;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
public class MinGradeMarkRequirementMapper implements
	Mapper<Map.Entry<String, MinMarkRequirementData>, Map.Entry<String, MinMarkRequirementData>, MinGradeMarkRequirement> {

    @Override
    public MinGradeMarkRequirement dataToEntity(Entry<String, MinMarkRequirementData> data) {
	return MinGradeMarkRequirement.builder()
		.grade(Grade.builder().code(data.getKey()).build())
		.value(data.getValue().getValue())
		.build();
    }

    @Override
    public Entry<String, MinMarkRequirementData> entityToData(MinGradeMarkRequirement entity) {
	return new ImmutablePair<>(entity.getGrade().getCode(),
		MinMarkRequirementData.builder().value(entity.getValue()).build());
    }

}
