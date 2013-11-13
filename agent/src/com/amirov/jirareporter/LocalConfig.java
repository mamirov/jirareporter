package com.amirov.jirareporter;



import com.amirov.jirareporter.teamcity.TeamCityXMLParser;

public class LocalConfig {

    public static String getTestResultText(){
        return TeamCityXMLParser.getStatusBuild()+"\nТесты завершились\nРезультаты:\n ["+Reporter.getBuildType()+" : "+TeamCityXMLParser.getBuildTestsStatus()+"|"+ "http://localhost:8111/viewLog.html?buildId="+TeamCityXMLParser.getBuildId()+"&tab=buildResultsDiv&buildTypeId="+Reporter.getBuildType()+"]";
    }


}
