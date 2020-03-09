package net.atlassian.cmathtutor.adaptive.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.atlassian.cmathtutor.adaptive.service.PredefinedTestService;

@ExtendWith(MockitoExtension.class)
class PredefinedTestControllerTest {

    private static final int CREATED_TEST_ID = 1;
    PredefinedTestController predefinedTestController;
    @Mock
    private PredefinedTestService predefinedTestService;

    @BeforeEach
    void setUp() {
	predefinedTestController = new PredefinedTestController(predefinedTestService);
    }

    @Test
    final void testInstallSimpleEnglishCredit() {
	when(predefinedTestService.createSimpleEnglishCreditTest()).thenReturn(CREATED_TEST_ID);

	assertThat(predefinedTestController.installSimpleEnglishCredit(), is(equalTo(CREATED_TEST_ID)));

	verify(predefinedTestService).createSimpleEnglishCreditTest();
    }

}
