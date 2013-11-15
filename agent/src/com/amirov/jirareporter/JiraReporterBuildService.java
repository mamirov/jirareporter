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
        for(Map.Entry<String, String> entry : new HashMap<>(getRunnerParameters()).entrySet()){
            RunnerParamsProvider.setProperty(entry.getKey(), entry.getValue());
        }
        if(RunnerParamsProvider.getIssueIdPlace().equals("teamcity")){
            if(RunnerParamsProvider.getIssueId().isEmpty() || RunnerParamsProvider.getIssueId() == null){
                System.out.println("issue id is empty");
            }
        }
        else if(RunnerParamsProvider.getIssueId().isEmpty() || RunnerParamsProvider.getIssueId() == null){
            System.out.println("issue id is empty");
        }
        else {
            Reporter.report();
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
