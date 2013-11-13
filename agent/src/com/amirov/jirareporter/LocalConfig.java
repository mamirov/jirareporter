package com.amirov.jirareporter;



import com.amirov.jirareporter.jira.JIRAConfig;
import com.amirov.jirareporter.teamcity.TeamCityConfig;
import com.amirov.jirareporter.teamcity.TeamCityXMLParser;

import java.io.FileInputStream;
import java.util.Properties;

public class LocalConfig {
    private static Properties props = new Properties();
    static {
        try{
            String local = JIRAConfig.getLocal();
            if(local.isEmpty() || local == null){
                local = "ru";
            }
            FileInputStream fis = new FileInputStream(local+"-local.properties");
            props.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTestResultText(){
        return TeamCityXMLParser.getStatusBuild()+"\n"+props.getProperty("build.finish")+"\n"+props.getProperty("results")+":\n ["+Reporter.getBuildType()+" : "+TeamCityXMLParser.getBuildTestsStatus()+"|"+ TeamCityConfig.getTeamCityServerURL()+"/viewLog.html?buildId="+TeamCityXMLParser.getBuildId()+"&tab=buildResultsDiv&buildTypeId="+Reporter.getBuildType()+"]";
    }


}
