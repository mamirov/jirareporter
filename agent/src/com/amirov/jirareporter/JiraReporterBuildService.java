package com.amirov.jirareporter;

import com.amirov.jirareporter.teamcity.TeamCityXMLParser;
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
//    private Map<String, String> runnerParams = new HashMap<>(getRunnerParameters());
    private static Map<String, String> runnerParams;

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        List<String> arguments = new ArrayList<>();
        Map<String, String> env = new HashMap<>();
        runnerParams = new HashMap<>(getRunnerParameters());
        TeamCityXMLParser.prepareTCConfiguration(runnerParams.get("tcServerURL"), runnerParams.get("tcUser"), runnerParams.get("tcPassword"), getBuild().getBuildTypeId());
        String issueIdPlace = runnerParams.get("issueIdPlace");
        Reporter.report(getIssueId(issueIdPlace, runnerParams.get("issueId")));

        String osName = System.getProperty("os.name");
        if(osName.contains("Mac") || osName.contains("nix") || osName.contains("nux")){
            return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "/bin/cat", arguments);
        }
        if(osName.contains("Win")){
            return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "C:\\Windows\\System32\\whoami.exe", arguments);
        }
        return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "", arguments);
    }

    public  static Map<String, String> getRunnerParams(){
        return runnerParams;
    }

    public String getIssueId(String issueIdPlace, String customIssue){
        String issueId = "";
        switch (issueIdPlace){
            case "teamcity":
                issueId = TeamCityXMLParser.getIssue();
                break;
            case "custom":
                issueId = customIssue;
                break;
        }
        return issueId;
    }
}
