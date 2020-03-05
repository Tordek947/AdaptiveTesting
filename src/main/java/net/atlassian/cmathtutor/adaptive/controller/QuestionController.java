package net.atlassian.cmathtutor.adaptive.controller;

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
import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionData;
import net.atlassian.cmathtutor.adaptive.domain.mapper.impl.QuestionMapper;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundApiResponse;
import net.atlassian.cmathtutor.adaptive.service.QuestionService;

@Tag(name = "Question", description = "Question management API")
@RestController
@RequestMapping("/rest/tests/{testId}/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;

    @Operation(summary = "Get question by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @NotFoundApiResponse
    public QuestionData getQuestionById(@PathVariable Integer testId, @PathVariable Integer id) {
	return questionMapper.entityToData(questionService.getById(id));
    }

    @Operation(summary = "Create question")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionData createQuestion(@PathVariable Integer testId, @RequestBody CreateQuestionData question) {
	return questionMapper.entityToData(questionService.create(questionMapper.dataToEntity(question), testId));
    }
}
