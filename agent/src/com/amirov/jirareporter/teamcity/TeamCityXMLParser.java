package com.amirov.jirareporter.teamcity;


import com.amirov.jirareporter.Reporter;
import com.amirov.jirareporter.RunnerParamsProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class TeamCityXMLParser {
    private static String SERVER_URL = RunnerParamsProvider.getTCServerUrl();
    private static String userPassword = RunnerParamsProvider.getTCUser()+":"+ RunnerParamsProvider.getTCPassword();

    private static NodeList getNodeList(String xmlUrl, String tag) {
        try{
            URL url = new URL(xmlUrl);
            validateTeamCityData();
            String encoding = new BASE64Encoder().encode(userPassword.getBytes());
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("Authorization","Basic " + encoding);
            uc.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(uc.getInputStream()));
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName(tag);
//            return nodeList.item(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static NamedNodeMap parseXML(String xmlUrl, String tag){
        RunnerParamsProvider.setProperty("build.xml.url", xmlUrl);
        return getNodeList(xmlUrl, tag).item(0).getAttributes();
    }

    public static String getBuildTypeId(){
        return Reporter.getBuildType();
    }

    private static NamedNodeMap getBuildData(){
        String BUILDS_XML_URL = "/httpAuth/app/rest/builds?locator=branch:default:any,running:true,buildType:";
        return parseXML(SERVER_URL+ BUILDS_XML_URL +getBuildTypeId(), "build");
    }

    private static String getBuildAttribute(String attribute){
        return getBuildData().getNamedItem(attribute).getNodeValue();
    }

    public static String getIssue(){
        StringBuilder sb = new StringBuilder();
        try{
            NodeList issueList = getNodeList(SERVER_URL +"/httpAuth/app/rest/builds/id:"+getBuildId()+"/relatedIssues", "issue");
            for(int i = 0; i<issueList.getLength(); i++){
                String issue = issueList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                if(sb.toString().contains(issue)){
                    System.out.println("Issue is duplicated");
                }
                else {
                    sb.append(issue+",");
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("Issue is not related");
        }
        return sb.toString();
    }

    public static String getStatusBuild(){
        return getBuildAttribute("status");
    }

    public static String getBranchName (){
        return getBuildAttribute("branchName");
    }

//    public static String getBuildTypeId(){
//        return getBuildAttribute("buildTypeId");
//    }

    public static String getBuildHref(){
        return getBuildAttribute("href");
    }

    public static  String getWebUrl(){
        return getBuildAttribute("webUrl");
    }

    public static String getBuildTestsStatus(){
        return getNodeList(SERVER_URL + getBuildHref(), "statusText").item(0).getTextContent();
    }

    public static String getBuildId(){
        return getBuildAttribute("id");
    }

    public static String getArtifactHref(){
        return getNodeList(SERVER_URL + getBuildHref(), "artifacts").item(0).getAttributes().getNamedItem("href").getNodeValue();
    }

    public static String getArtifactName(){
        return getNodeList(SERVER_URL + getArtifactHref(), "file").item(0).getAttributes().getNamedItem("name").getNodeValue();
    }

    private static void validateTeamCityData(){
        if(SERVER_URL.isEmpty()){
            System.out.println("TeamCity server url is empty");
        }
        if(userPassword.isEmpty()){
            System.out.println("TeamCity user or password is empty");
        }
    }

    public static String getTestResultText(){
        if(RunnerParamsProvider.enableCommentTemplate().equals("true")){
            return getTemplateComment();
        }
        else {
            return getStatusBuild()+"\nBuild Finished\nResults:\n ["+RunnerParamsProvider.getBuildTypeName()+" : "+getBuildTestsStatus()+"|"+SERVER_URL +"/viewLog.html?buildId="+getBuildId()+"&tab=buildResultsDiv&buildTypeId="+ Reporter.getBuildType()+"]";
        }
    }

    public static String getTemplateValue(String param){
        String value = "";
        switch (param){
            case "*status.build*":
                value = getStatusBuild();
                break;
            case "*build.type.name*":
                value = RunnerParamsProvider.getBuildTypeName();
                break;
            case "*tests.results*":
                value = getBuildTestsStatus();
                break;
            case "*teamcity.server.url*":
                value = SERVER_URL;
                break;
            case "*build.id*":
                value = getBuildId();
                break;
            case "*build.type*":
                value = Reporter.getBuildType();
                break;
        }
        return value;
    }

    public static String getTemplateComment(){
        String template = RunnerParamsProvider.getTemplateComment();
        List<String> templateParams = Arrays.asList("*status.build*", "*build.type.name*", "*tests.results*", "*teamcity.server.url*", "*build.id*", "*build.type*");
        for(String param : templateParams){
            if(template.contains(param)){
                template = template.replace(param, getTemplateValue(param));
            }
        }
        return template;
    }
}
