package com.amirov.jirareporter;


import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildRunnerContext;

public class RunnerParamsProvider {
    private BuildRunnerContext buildRunnerContext;
    private static BuildAgentConfiguration agentConfiguration;

    public static String getTCServerURL(){
        return agentConfiguration.getServerUrl();
    }

    public String getBuildType(){
        return buildRunnerContext.getRunnerParameters().get("buildTypeId");
    }

    public String getJiraServerUrl(){
        return buildRunnerContext.getRunnerParameters().get("jiraServerUrl");
    }

    public boolean progressIssueIsEnable(){
        return Boolean.parseBoolean(buildRunnerContext.getRunnerParameters().get("enableProgressIssue"));
    }
}
