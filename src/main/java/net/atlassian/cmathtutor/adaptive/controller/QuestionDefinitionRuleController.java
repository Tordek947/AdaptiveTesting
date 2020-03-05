package net.atlassian.cmathtutor.adaptive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;
import net.atlassian.cmathtutor.adaptive.domain.mapper.impl.QuestionDefinitionRuleMapper;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundApiResponse;
import net.atlassian.cmathtutor.adaptive.service.QuestionDefinitionRuleService;

@Tag(name = "QuestionDefinitionRule", description = "QuestionDefinitionRule management API")
@RestController
@RequestMapping("/rest/tests/{testId}/questionDefinitionRules")
public class QuestionDefinitionRuleController {

    @Autowired
    private QuestionDefinitionRuleService questionDefinitionRuleService;
    @Autowired
    private QuestionDefinitionRuleMapper questionDefinitionRuleMapper;

    @Operation(summary = "Get all question definition rules for testId")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @NotFoundApiResponse
    public List<QuestionDefinitionRuleData> getAllQuestionDefinitionRules(@PathVariable Integer testId) {
	return Mapper.collectionToList(questionDefinitionRuleService.getAllByTestId(testId),
		questionDefinitionRuleMapper::entityToData);
    }

    @Operation(summary = "Create question definition rule")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDefinitionRuleData createQuestion(@PathVariable Integer testId,
	    @RequestBody CreateQuestionDefinitionRuleData questionDefinitionRule) {
	return questionDefinitionRuleMapper.entityToData(questionDefinitionRuleService
		.create(questionDefinitionRuleMapper.dataToEntity(questionDefinitionRule), testId));
    }
}
