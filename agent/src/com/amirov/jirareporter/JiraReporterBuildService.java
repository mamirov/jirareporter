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
        List<String> arguments = new ArrayList<String>();
        Map<String, String> runnerParams = new HashMap<String, String>(getRunnerParameters());
        Map<String, String> env = new HashMap<String, String>();
        Reporter.report();
        Reporter.progressIssue();

        String osName = System.getProperty("os.name");
        if(osName.contains("Mac") || osName.contains("nix")){
            return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "/bin/cat", arguments);
        }
        if(osName.contains("Win")){
            return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "C:\\Windows\\System32\\whoami.exe", arguments);
        }
        return new SimpleProgramCommandLine(env, getWorkingDirectory().getAbsolutePath(), "", arguments);
    }
}
