package net.atlassian.cmathtutor.adaptive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.data.CreateTestData;
import net.atlassian.cmathtutor.adaptive.domain.data.TestData;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;
import net.atlassian.cmathtutor.adaptive.domain.mapper.impl.TestMapper;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundApiResponse;
import net.atlassian.cmathtutor.adaptive.service.TestService;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Test", description = "Test management API")
@RestController
@RequestMapping("/rest/tests")
public class TestController {

    private TestService testService;
    private TestMapper testMapper;

    @Operation(summary = "Get test by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @NotFoundApiResponse
    public TestData getTestById(@PathVariable Integer id) {
	return testMapper.entityToData(testService.getById(id));
    }

    @Operation(summary = "Get test by name")
    @GetMapping(path = "/by", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @NotFoundApiResponse
    public TestData getTestByName(@RequestParam String name) {
	return testMapper.entityToData(testService.getByName(name));
    }

    @Operation(summary = "Get all tests")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public List<TestData> getAllTests() {
	return Mapper.collectionToList(testService.getAll(), testMapper::entityToData);
    }

    @Operation(summary = "Create test")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TestData createTest(@RequestBody CreateTestData test) {
	return testMapper.entityToData(testService.create(testMapper.dataToEntity(test)));
    }

    @Operation(summary = "Update test")
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
	    produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @NotFoundApiResponse
    public TestData updateTest(@PathVariable Integer id, @RequestBody String testName) {
	return testMapper.entityToData(testService.updateNameById(testName, id));
    }

    @Operation(summary = "Delete test")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "Successful operation")
    @NotFoundApiResponse
    public void deleteTest(@PathVariable Integer id) {
	testService.deleteById(id);
    }
}
