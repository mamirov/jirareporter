package com.amirov.jirareporter;

import com.amirov.jirareporter.jira.JIRAConfig;
import com.amirov.jirareporter.teamcity.TeamCityXMLParser;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;

import static com.amirov.jirareporter.jira.JIRAClient.*;

public class Reporter {

    private static String issueId;

    private static RunnerParamsProvider params = new RunnerParamsProvider();

    public static String getBuildType(){
        return JiraReporterBuildService.getBuildTypeId();
    }

    public static String getIssueId(){
        String issueIdPlace = params.getIssueIdPlace();
        switch (issueIdPlace){
            case "teamcity":
                issueId = TeamCityXMLParser.getIssue();
                break;
            case "custom":
                issueId = params.getIssueId();
                break;
        }
        issueIdAlert();
        return issueId;
    }

    public static void report(){
        System.out.println("ISSUE: "+getIssueId());
        System.out.println("Title: "+getIssue().getSummary());
        System.out.println("Description: "+getIssue().getDescription());
        NullProgressMonitor pm = new NullProgressMonitor();
        getRestClient().getIssueClient().addComment(pm, getIssue().getCommentsUri(), Comment.valueOf(LocalConfig.getTestResultText()));
    }

    public static void progressIssue(){
        NullProgressMonitor pm = new NullProgressMonitor();
        if(params.progressIssueIsEnable()){
            String teamCityBuildStatus = TeamCityXMLParser.getStatusBuild();
            getRestClient().getIssueClient().transition(getIssue().getTransitionsUri(), getTransitionInput(JIRAConfig.prepareJiraWorkflow(teamCityBuildStatus).get(getIssueStatus())), pm);
        }
    }

    private static void issueIdAlert(){
        if(issueId == null || issueId.isEmpty()){
            System.out.println("Issue id is empty");
            System.exit(0);
        }
    }
}
