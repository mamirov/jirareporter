package com.amirov.jirareporter;

import com.amirov.jirareporter.jira.JIRAConfig;
import com.amirov.jirareporter.teamcity.TeamCityXMLParser;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;
import jetbrains.buildServer.agent.BuildProgressLogger;

import static com.amirov.jirareporter.jira.JIRAClient.*;

public class Reporter {

    public static void report(BuildProgressLogger logger){
        logger.message("ISSUE: "+RunnerParamsProvider.getIssueId()
                +"\nTitle: "+getIssue().getSummary()
                +"\nDescription: "+getIssue().getDescription());
        NullProgressMonitor pm = new NullProgressMonitor();
        getRestClient().getIssueClient().addComment(pm, getIssue().getCommentsUri(), Comment.valueOf(TeamCityXMLParser.getTestResultText()));
    }

    public static void progressIssue(){
        NullProgressMonitor pm = new NullProgressMonitor();
        if(RunnerParamsProvider.progressIssueIsEnable() == null){}
        else if(RunnerParamsProvider.progressIssueIsEnable().equals("true")){
            String teamCityBuildStatus = TeamCityXMLParser.getStatusBuild();
            getRestClient().getIssueClient().transition(getIssue().getTransitionsUri(), getTransitionInput(JIRAConfig.prepareJiraWorkflow(teamCityBuildStatus).get(getIssueStatus())), pm);
        }
    }
}
