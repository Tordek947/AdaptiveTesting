package net.atlassian.cmathtutor.adaptive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.data.parameter.DecisionMakingParameter;
import net.atlassian.cmathtutor.adaptive.domain.data.parameter.TestStateParameter;
import net.atlassian.cmathtutor.adaptive.service.TestProcessService;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "TestProcess", description = "Test process definition API")
@RestController
@RequestMapping("/rest/testprocesses")
public class TestProcessController {

    private TestProcessService testProcessService;

    @Operation(summary = "Get available questions for current test state")
    @PostMapping(path = "/questions/available", consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ResponseBody
    public DecisionMakingParameter getAvailableQuestionsForCurrentTestState(@RequestBody TestStateParameter testState) {
	return testProcessService.getAvailableQuestionsForCurrentTestState(testState);
    }
}
