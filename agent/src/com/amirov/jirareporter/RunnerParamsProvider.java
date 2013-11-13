package com.amirov.jirareporter;



import java.util.Map;

public class RunnerParamsProvider {
    private Map<String, String> buildRunnerContext = JiraReporterBuildService.getRunnerParams();

    public String getJiraServerUrl(){
        return buildRunnerContext.get("jiraServerUrl");
    }

    public String getJiraUser(){
        return buildRunnerContext.get("jiraUser");
    }

    public String getJiraPassword(){
        return buildRunnerContext.get("jiraPassword");
    }

    public String sslConnectionIsEnabled(){
        if(buildRunnerContext.get("enableSSLConnection").equals("true")){
            return "false";
        }
        else {
            return "true";
        }
    }

    public String getIssueIdPlace(){
        return buildRunnerContext.get("issueIdPlace");
    }

    public String getIssueId(){
        return buildRunnerContext.get("issueId");
    }

    public String getJiraWorkFlow(){
        return buildRunnerContext.get("jiraWorkflow");
    }

    public boolean progressIssueIsEnable(){
        return Boolean.parseBoolean(buildRunnerContext.get("enableIssueProgressing"));
    }
}
