package net.atlassian.cmathtutor.adaptive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.service.PredefinedTestService;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Predefined Test", description = "Install some predefined packages of tests")
@RestController
@RequestMapping("/rest/tests")
public class PredefinedTestController {

    private PredefinedTestService predefinedTestService;

    @Operation(summary = "Create and fully initialize predefined 'Simple English Credit' test")
    @PostMapping(path = "/simple-english-credit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Integer installSimpleEnglishCredit() {
	return predefinedTestService.createSimpleEnglishCreditTest();
    }
}
