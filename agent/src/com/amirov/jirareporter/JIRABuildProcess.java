/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
