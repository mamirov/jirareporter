package com.amirov.jirareporter;

import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class JiraReporterRunType extends RunType{

    public JiraReporterRunType(final RunTypeRegistry registry){
        registry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return "jirareporter";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "JIRA Reporter";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Reporting build results to JIRA issue";
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return null;
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return "jirareporter.jsp";
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return "jirareporter.jsp";
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return null;
    }
}
