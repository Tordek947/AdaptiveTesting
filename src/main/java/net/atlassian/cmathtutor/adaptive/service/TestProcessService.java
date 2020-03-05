package net.atlassian.cmathtutor.adaptive.service;

import net.atlassian.cmathtutor.adaptive.domain.data.parameter.DecisionMakingParameter;
import net.atlassian.cmathtutor.adaptive.domain.data.parameter.TestStateParameter;

public interface TestProcessService {

    DecisionMakingParameter getAvailableQuestionsForCurrentTestState(TestStateParameter testState);
}
