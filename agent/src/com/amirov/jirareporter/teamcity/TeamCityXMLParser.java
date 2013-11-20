package com.amirov.jirareporter.teamcity;


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

public class TeamCityXMLParser {
    private static String SERVER_URL = RunnerParamsProvider.getTCServerUrl();
    private static String BUILD_TYPE = RunnerParamsProvider.getBuildTypeId();
    private static String BUILDS_XML_URL = "/httpAuth/app/rest/builds?locator=branch:default:any,running:true,buildType:"+ BUILD_TYPE;
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

    private static NamedNodeMap getBuildData(){
        return parseXML(SERVER_URL+BUILDS_XML_URL, "build");
    }

    private static NamedNodeMap parseXML(String xmlUrl, String tag){
        return getNodeList(xmlUrl, tag).item(0).getAttributes();
    }

    private static String getBuildAttribute(String attribute){
        return getBuildData().getNamedItem(attribute).getNodeValue();
    }

    public static String getIssue(){
        try{
            NodeList issueList = getNodeList(SERVER_URL +"/httpAuth/app/rest/builds/id:"+getBuildId()+"/relatedIssues", "issue");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<issueList.getLength(); i++){
                String issue = issueList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                if(sb.toString().contains(issue)){
                    System.out.println("Issue is duplicated");
                }
                else {
                    sb.append(issue+",");
                }
            }
            return sb.toString();
        }
        catch (NullPointerException e){
            System.out.println("Issue is not related");
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
        return getStatusBuild()+"\nBuild Finished\nResults:\n ["+RunnerParamsProvider.getBuildTypeName()+" : "+getBuildTestsStatus()+"|"+SERVER_URL +"/viewLog.html?buildId="+getBuildId()+"&tab=buildResultsDiv&buildTypeId="+BUILD_TYPE+"]";
    }
}
