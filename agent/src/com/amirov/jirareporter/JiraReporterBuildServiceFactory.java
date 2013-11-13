package com.amirov.jirareporter;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.jetbrains.annotations.NotNull;

public class JiraReporterBuildServiceFactory implements CommandLineBuildServiceFactory {
    @NotNull
    @Override
    public CommandLineBuildService createService() {
        return new JiraReporterBuildService();
    }

    @NotNull
    @Override
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new AgentBuildRunnerInfo() {
            @NotNull
            @Override
            public String getType() {
                return "jirareporter";
            }

            @Override
            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
                return true;
            }
        };
    }

}
