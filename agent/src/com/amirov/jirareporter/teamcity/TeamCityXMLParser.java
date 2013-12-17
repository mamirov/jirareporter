package com.amirov.jirareporter.teamcity;


import com.amirov.jirareporter.RunnerParamsProvider;
import com.google.common.collect.ImmutableMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class TeamCityXMLParser {
    private String SERVER_URL = RunnerParamsProvider.getTCServerUrl();
    private String userPassword = RunnerParamsProvider.getTCUser()+":"+ RunnerParamsProvider.getTCPassword();
    private NamedNodeMap buildData;
    public String buildTypeId = RunnerParamsProvider.getBuildTypeId();
    
    public TeamCityXMLParser(){
        String BUILDS_XML_URL = "/httpAuth/app/rest/builds?locator=branch:default:any,running:true,buildType:";
        buildData = parseXML(SERVER_URL+ BUILDS_XML_URL +buildTypeId, "build");
    }

    private NodeList getNodeList(String xmlUrl, String tag) {
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

    private NamedNodeMap parseXML(String xmlUrl, String tag){
        RunnerParamsProvider.setProperty("build.xml.url", xmlUrl);
        return getNodeList(xmlUrl, tag).item(0).getAttributes();
    }

    private String getBuildAttribute(String attribute){
        return buildData.getNamedItem(attribute).getNodeValue();
    }

    public String getIssue(){
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

    public String getStatusBuild(){
        return getBuildAttribute("status");
    }

    public String getBranchName (){
        return getBuildAttribute("branchName");
    }

//    public String getBuildTypeId(){
//        return getBuildAttribute("buildTypeId");
//    }

    public String getBuildHref(){
        return getBuildAttribute("href");
    }

    public  String getWebUrl(){
        return getBuildAttribute("webUrl");
    }

    public String getBuildTestsStatus(){
        return getNodeList(SERVER_URL + getBuildHref(), "statusText").item(0).getTextContent();
    }

    public String getBuildId(){
        return getBuildAttribute("id");
    }

    public String getArtifactHref(){
        return getNodeList(SERVER_URL + getBuildHref(), "artifacts").item(0).getAttributes().getNamedItem("href").getNodeValue();
    }

    public String getArtifactName(){
        return getNodeList(SERVER_URL + getArtifactHref(), "file").item(0).getAttributes().getNamedItem("name").getNodeValue();
    }

    private void validateTeamCityData(){
        if(SERVER_URL.isEmpty()){
            System.out.println("TeamCity server url is empty");
        }
        if(userPassword.isEmpty()){
            System.out.println("TeamCity user or password is empty");
        }
    }

    public String getTestResultText(){
        if(RunnerParamsProvider.enableCommentTemplate().equals("true")){
            return getTemplateComment();
        }
        else {
            return getStatusBuild()+"\nBuild Finished\nResults:\n ["+RunnerParamsProvider.getBuildTypeName()+" : "+getBuildTestsStatus()+"|"+SERVER_URL +"/viewLog.html?buildId="+getBuildId()+"&tab=buildResultsDiv&buildTypeId="+ buildTypeId+"]";
        }
    }

    public ImmutableMap<String, String> getTemplateValue(){
        return new ImmutableMap.Builder<String, String>()
                .put("*status.build*", getStatusBuild())
                .put("*build.type.name*", RunnerParamsProvider.getBuildTypeName())
                .put("*tests.results*", getBuildTestsStatus())
                .put("*teamcity.server.url*", SERVER_URL)
                .put("*build.id*", getBuildId())
                .put("*build.type*", buildTypeId)
                .build();
    }

    public String getTemplateComment(){
        String template = RunnerParamsProvider.getTemplateComment();
        for(Map.Entry<String, String> entry : getTemplateValue().entrySet()){
            if(template.contains(entry.getKey())){
                template = template.replace(entry.getKey(), entry.getValue());
            }
        }
        return template;
    }
}
