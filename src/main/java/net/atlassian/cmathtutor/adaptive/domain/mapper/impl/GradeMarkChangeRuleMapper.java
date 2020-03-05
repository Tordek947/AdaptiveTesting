package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;

import net.atlassian.cmathtutor.adaptive.domain.data.CreateMarkChangeRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.MarkChangeRuleData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
public class GradeMarkChangeRuleMapper implements
	Mapper<Map.Entry<String, CreateMarkChangeRuleData>, Map.Entry<String, MarkChangeRuleData>, GradeMarkChangeRule> {

    @Override
    public GradeMarkChangeRule dataToEntity(Entry<String, CreateMarkChangeRuleData> data) {
	return GradeMarkChangeRule.builder()
		.grade(Grade.builder().code(data.getKey()).build())
		.markDelta(data.getValue().getMarkDelta())
		.build();
    }

    @Override
    public Entry<String, MarkChangeRuleData> entityToData(GradeMarkChangeRule entity) {
	return new ImmutablePair<>(entity.getGrade().getCode(),
		MarkChangeRuleData.builder()
			.id(entity.getId()).markDelta(entity.getMarkDelta())
			.build());
    }

}
