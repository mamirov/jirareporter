package com.amirov.jirareporter;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JiraReporterBuildService extends BuildServiceAdapter{

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        List<String> arguments = new ArrayList<>();
        Map<String, String> env = new HashMap<>();
        Map<String, String> runnerParams = new HashMap<>(getRunnerParameters());
        for(Map.Entry<String, String> entry : runnerParams.entrySet()){
            RunnerParamsProvider.setProperty(entry.getKey(), entry.getValue());
        }
        if(runnerParams.get("enableIssueProgressing") == null){
            RunnerParamsProvider.setProperty("enableIssueProgressing", "false");
        }
        if(runnerParams.get("enableSSLConnection") == null){
            RunnerParamsProvider.setProperty("enableSSLConnection", "false");
        }
        RunnerParamsProvider.setProperty("buildName", getBuild().getBuildTypeName());
        String issueId = RunnerParamsProvider.getIssueId();
        if(issueId == null || issueId.isEmpty()){
            getLogger().message("Issue is not related");
        }
        else {
            if(issueId.contains(",")){
                for(String issue : issueId.split(",")){
                    Reporter.report(getLogger(), issue);
                    Reporter.progressIssue();
                }
            }
            else {
                Reporter.report(getLogger(), issueId);
                Reporter.progressIssue();
            }
        }
        String osName = System.getProperty("os.name");
        if(osName.contains("Mac") || osName.contains("nix") || osName.contains("nux")){
            return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "/bin/cat", arguments);
        }
        if(osName.contains("Win")){
            return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "C:\\Windows\\System32\\whoami.exe", arguments);
        }
        return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "", arguments);
    }
}
