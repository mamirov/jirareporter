package com.amirov.jirareporter.jira;


import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JIRAConfig {
    private static Properties props = new Properties();
    static {
        try{
            FileInputStream fis = new FileInputStream("jira.properties");
            props.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJiraUrl(){
        return props.getProperty("jira.server.url");
    }

    public static String getJiraUser(){
        return props.getProperty("jira.user");
    }

    public static String getJiraPassword(){
        return props.getProperty("jira.password");
    }

    public static String jiraHttpsConnectionIsEnabled(){
        return props.getProperty("jira.https.connection.enable");
    }

    public static String getJiraWorkflow(){
        return props.getProperty("jira.workflow");
    }

    public static String getIssueId(){
        return props.getProperty("issueId");
    }

    public static Boolean issueProgressingIsEnable(){
        return Boolean.parseBoolean(props.getProperty("enable.jira.progress.issue"));
    }

    public static String getIssuePlace(){
        return props.getProperty("issue.id.place");
    }

    public static String getLocal(){
        return props.getProperty("local");
    }

    private static void processWorkflow(String buildStatus, String propStatus, Map<String, String> workFlowMap){
        if(propStatus.contains(buildStatus)){
            String [] progressSteps = propStatus.split(":");
            String progressStep = progressSteps[progressSteps.length-1];
            String [] transitions = progressStep.split(",");
            for(String transition : transitions){
                String [] steps = transition.split("-");
                String key = steps[steps.length-2];
                String value = steps[steps.length-1];
                workFlowMap.put(key, value);
            }
        }
    }

    public static Map<String, String> prepareJiraWorkflow(String buildStatus){
        Map<String, String> successWorkflowMap = new HashMap<>();
        Map<String, String> failureWorkflowMap = new HashMap<>();
        String [] statusCont = getJiraWorkflow().split(";");
        for(String status : statusCont){
            processWorkflow("SUCCESS", status, successWorkflowMap);
            processWorkflow("FAILURE", status, failureWorkflowMap);
        }
        if(buildStatus.equals("SUCCESS")){
            return successWorkflowMap;
        }
        else {
            return failureWorkflowMap;
        }
    }
}
