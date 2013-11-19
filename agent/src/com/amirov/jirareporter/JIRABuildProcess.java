
package com.amirov.jirareporter;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class JIRABuildProcess implements BuildProcess{
    private final AgentRunningBuild myBuild;
    private final BuildRunnerContext myContext;
    private BuildProgressLogger logger;


    public JIRABuildProcess(@NotNull AgentRunningBuild build, @NotNull BuildRunnerContext context){
        myBuild = build;
        myContext = context;
    }

    @Override
    public void start() throws RunBuildException {
        logger = myBuild.getBuildLogger();
        Map<String, String> runnerParams = new HashMap<>(myContext.getRunnerParameters());
        for(Map.Entry<String, String> entry : runnerParams.entrySet()){
            RunnerParamsProvider.setProperty(entry.getKey(), entry.getValue());
        }
        if(runnerParams.get("enableIssueProgressing") == null){
            RunnerParamsProvider.setProperty("enableIssueProgressing", "false");
        }
        if(runnerParams.get("enableSSLConnection") == null){
            RunnerParamsProvider.setProperty("enableSSLConnection", "false");
        }
        RunnerParamsProvider.setProperty("buildName", myContext.getBuild().getBuildTypeName());
        String issueId = RunnerParamsProvider.getIssueId();
        if(issueId == null || issueId.isEmpty()){
            logger.message("Issue is not related");
        }
        else {
            if(issueId.contains(",")){
                for(String issue : issueId.split(",")){
                    Reporter.report(logger, issue);
                    Reporter.progressIssue();
                }
            }
            else {
                Reporter.report(logger, issueId);
                Reporter.progressIssue();
            }
        }

    }

    @Override
    public boolean isInterrupted() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void interrupt() {
    }

    @NotNull
    @Override
    public BuildFinishedStatus waitFor() throws RunBuildException {
        return BuildFinishedStatus.FINISHED_SUCCESS;
    }
}
