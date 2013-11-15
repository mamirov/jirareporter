package com.amirov.jirareporter;



import com.amirov.jirareporter.teamcity.TeamCityXMLParser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public  class RunnerParamsProvider {
    public  static Properties props = new Properties();
    static {
        try {
            FileInputStream fis = new FileInputStream("params.properties");
            props.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(String key, String value){
        try{
            props.setProperty(key, value);
            props.store(new FileOutputStream("params.properties"), "set parameters");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJiraServerUrl(){
        return props.getProperty("jiraServerUrl");
    }

    public static String getJiraUser(){
        return props.getProperty("jiraUser");
    }

    public static String getJiraPassword(){
        return props.getProperty("jiraPassword");
    }

    public static String sslConnectionIsEnabled(){
        if(props.getProperty("enableSSLConnection").equals("true")){
            return "false";
        }
        else {
            return "true";
        }
    }

    public static String getBuildTypeId(){
        return props.getProperty("teamcity.buildType.id");
    }

    public static String getIssueIdPlace(){
        return props.getProperty("issueIdPlace");
    }

    public static String getIssueId(){
        String issueId = "";
        switch (getIssueIdPlace()){
            case "teamcity":
                String issueTC = TeamCityXMLParser.getIssue();
                setProperty("issueId", issueTC);
                issueId = issueTC;
                break;
            case "custom":
                issueId = props.getProperty("issueId");
        }
        return issueId;
    }

    public static String getJiraWorkFlow(){
        return props.getProperty("jiraWorkflow");
    }

    public static String getTCServerUrl(){
        return props.getProperty("tcServerUrl");
    }

    public static String getTCUser(){
        return props.getProperty("tcUser");
    }

    public static String getTCPassword(){
        return props.getProperty("tcPassword");
    }

    public static String progressIssueIsEnable(){
        return props.getProperty("enableIssueProgressing");
    }

    public static String getBuildTypeName(){
        return props.getProperty("buildName");
    }
}
