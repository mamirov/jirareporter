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

    private static RunnerParamsProvider params = new RunnerParamsProvider();
    private static String issueIdJira;

    public static JiraRestClient getRestClient() {
        System.setProperty("jsse.enableSNIExtension", params.sslConnectionIsEnabled());
        JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
        URI jiraServerUri = null;
        try {
            jiraServerUri = new URI(params.getJiraServerUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return factory.createWithBasicHttpAuthentication(jiraServerUri, params.getJiraUser(), params.getJiraPassword());
    }

    public static Issue getIssue(String issueId) {
        NullProgressMonitor pm = new NullProgressMonitor();
        Issue issue = null;
        try {
            if(issueId.isEmpty()){
                System.out.println("Issue id is empty");
            }
            issueIdJira = issueId;
            issue = getRestClient().getIssueClient().getIssue(issueId, pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return issue;
    }

    public static String getIssueStatus(){
        return getIssue(issueIdJira).getStatus().getName();
    }

    private static Iterable<Transition> getTransitions (){
        return getRestClient().getIssueClient().getTransitions(getIssue(issueIdJira).getTransitionsUri(), new NullProgressMonitor());
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
