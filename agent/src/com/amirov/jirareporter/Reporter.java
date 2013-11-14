package com.amirov.jirareporter;

import com.amirov.jirareporter.jira.JIRAConfig;
import com.amirov.jirareporter.teamcity.TeamCityXMLParser;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;

import static com.amirov.jirareporter.jira.JIRAClient.*;

public class Reporter {

    private static String issueIdJira;

    private static RunnerParamsProvider params = new RunnerParamsProvider();

    public static void report(String issueId){
        issueIdJira = issueId;
        System.out.println("ISSUE: "+issueId);
        System.out.println("Title: "+getIssue(issueId).getSummary());
        System.out.println("Description: "+getIssue(issueId).getDescription());
        NullProgressMonitor pm = new NullProgressMonitor();
        getRestClient().getIssueClient().addComment(pm, getIssue(issueId).getCommentsUri(), Comment.valueOf(TeamCityXMLParser.getTestResultText()));
    }

    public static void progressIssue(){
        NullProgressMonitor pm = new NullProgressMonitor();
        if(params.progressIssueIsEnable()){
            String teamCityBuildStatus = TeamCityXMLParser.getStatusBuild();
            getRestClient().getIssueClient().transition(getIssue(issueIdJira).getTransitionsUri(), getTransitionInput(JIRAConfig.prepareJiraWorkflow(teamCityBuildStatus).get(getIssueStatus())), pm);
        }
    }

    private static void issueIdAlert(){
        if(issueIdJira == null || issueIdJira.isEmpty()){
            System.out.println("Issue id is empty");
        }
    }
}
