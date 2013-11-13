package com.amirov.jirareporter.teamcity;


import com.amirov.jirareporter.Reporter;
import com.amirov.jirareporter.RunnerParamsProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TeamCityXMLParser {
    private static String SERVER_URL = RunnerParamsProvider.getTCServerURL();
    private static String BUILDS_XML_URL = "/httpAuth/app/rest/builds?locator=running:true,buildType:"+ Reporter.getBuildType();
    private static NamedNodeMap buildData = parseXML(SERVER_URL + BUILDS_XML_URL, "build");
    private static String userPassword;

    private static Node getNode(String xmlUrl, String tag, int num) {
        try{
            URL url = new URL(xmlUrl);
            userPassword = TeamCityConfig.getBasicAuth();
            validateTeamCityData();
            String encoding = new BASE64Encoder().encode(userPassword.getBytes());
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("Authorization","Basic " + encoding);
            uc.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(uc.getInputStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(tag);
            return nodeList.item(num);
        } catch (MalformedURLException e) {
            System.out.println("Enter valid url, example : http://teamcity.example.com");
            e.printStackTrace();
        } catch (IOException e) {
            if(e.getLocalizedMessage().contains("401")){
                System.out.println("Wrong user or password, check property 'teamcity.basic.auth.user.password' in teamcity.properties");
            }
            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static NamedNodeMap parseXML(String xmlUrl, String tag){
        try {
            return getNode(xmlUrl, tag, 0).getAttributes();
        }
        catch (NullPointerException e){
            System.out.println("Cannot find running builds for "+Reporter.getBuildType());
            System.exit(0);
        }
        return null;
    }

    private static String getBuildAttribute(String attribute){
        return buildData.getNamedItem(attribute).getNodeValue();
    }

    public static String getIssue(){
        try{
            Node issueTag = parseXML(SERVER_URL +"/httpAuth/app/rest/builds/id:"+getBuildId()+"/relatedIssues", "issue").getNamedItem("id");
            return issueTag.getNodeValue();
        }
        catch (NullPointerException e){
            System.out.println("Issue is not related");
            System.exit(0);
        }
        return "";
    }

    public static String getStatusBuild(){
        return getBuildAttribute("status");
    }

    public static String getBranchName (){
        return getBuildAttribute("branchName");
    }

    public static String getBuildTypeId(){
        return getBuildAttribute("buildTypeId");
    }

    public static String getBuildHref(){
        return getBuildAttribute("href");
    }

    public static  String getWebUrl(){
        return getBuildAttribute("webUrl");
    }

    public static String getBuildTestsStatus(){
        return getNode(SERVER_URL + getBuildHref(), "statusText", 0).getTextContent();
    }

    public static String getBuildId(){
        return getBuildAttribute("id");
    }

    public static String getArtifactHref(){
        return getNode(SERVER_URL + getBuildHref(), "artifacts", 0).getAttributes().getNamedItem("href").getNodeValue();
    }

    public static String getArtifactName(){
        return getNode(SERVER_URL + getArtifactHref(), "file", 0).getAttributes().getNamedItem("name").getNodeValue();
    }

    private static void validateTeamCityData(){
        if(SERVER_URL.isEmpty() || SERVER_URL == null){
            System.out.println("Enter teamcity server url in teamcity.properties");
        }
        if(userPassword.isEmpty() || userPassword == null){
            System.out.println("Enter user and password in teamcity.properties");
        }
    }
}
