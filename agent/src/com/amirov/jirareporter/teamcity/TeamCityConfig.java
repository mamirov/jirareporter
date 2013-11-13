package com.amirov.jirareporter.teamcity;

import java.io.FileInputStream;
import java.util.Properties;

public class TeamCityConfig {
    private static Properties props = new Properties();
    static {
        try{
            FileInputStream fis = new FileInputStream("teamcity.properties");
            props.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTeamCityServerURL(){
        return props.getProperty("teamcity.server.url");
    }

    public static String getBasicAuth(){
        return props.getProperty("teamcity.basic.auth.user.password");
    }
}
