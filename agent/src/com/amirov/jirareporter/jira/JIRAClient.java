package com.amirov.jirareporter.jira;

import com.amirov.jirareporter.RunnerParamsProvider;
import com.amirov.jirareporter.teamcity.TeamCityXMLParser;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Transition;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class JIRAClient {

    public static JiraRestClient getRestClient() {
        System.setProperty("jsse.enableSNIExtension", RunnerParamsProvider.sslConnectionIsEnabled());
        JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
        URI jiraServerUri = null;
        try {
            jiraServerUri = new URI(RunnerParamsProvider.getJiraServerUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return factory.createWithBasicHttpAuthentication(jiraServerUri, RunnerParamsProvider.getJiraUser(), RunnerParamsProvider.getJiraPassword());
    }

    public static Issue getIssue() {
        NullProgressMonitor pm = new NullProgressMonitor();
        Issue issue = null;
        try {
            if(RunnerParamsProvider.getIssueId().isEmpty()){
                System.out.println("Issue id is empty");
            }
            issue = getRestClient().getIssueClient().getIssue(RunnerParamsProvider.getIssueId(), pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issue;
    }

    public static String getIssueStatus(){
        return getIssue().getStatus().getName();
    }

    private static Iterable<Transition> getTransitions (){
        return getRestClient().getIssueClient().getTransitions(getIssue().getTransitionsUri(), new NullProgressMonitor());
    }

    private static Transition getTransition(String transitionName){
        return getTransitionByName(getTransitions(), transitionName);
    }

    public static TransitionInput getTransitionInput(String transitionName){
        return new TransitionInput(getTransition(transitionName).getId(), Comment.valueOf(TeamCityXMLParser.getTestResultText()));
    }

    private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName) {
        for (Transition transition : transitions) {
            if (transition.getName().equals(transitionName)) {
                return transition;
            }
        }
        return null;
    }
}
