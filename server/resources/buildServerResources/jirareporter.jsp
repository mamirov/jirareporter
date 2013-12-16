<%@ include file="/include.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%--<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>--%>
<%--<jsp:useBean id="xsollaProp" type="xsollacustomrunner.server.XsollaBean"/>--%>

<c:set var="title" value="JIRA Reporter" scope="request"/>


<l:settingsGroup title="JIRA configuration">
    <tr>
        <td>
            <span>Server URL:</span>
            <br>
            <props:textProperty name="jiraServerUrl" id="jiraServerURL" className="longField"/>
            <br>
            <span>User:</span>
            <br>
            <props:textProperty name="jiraUser" id="jiraUser"/>
            <br>
            <span>Password:</span>
            <br>
            <props:passwordProperty name="jiraPassword"/>
            <br>
            <span>Get issue from:</span>
            <props:selectProperty name="issueIdPlace">
                <props:option value="teamcity">VCS comment</props:option>
                <props:option value="custom">JIRA Reporter</props:option>
            </props:selectProperty>
            <br>
            <div id="jira.issue.id" style="display: none">
                <span>ISSUE ID:</span>
                <br>
                <props:textProperty name="issueId" id="issueId"/>
            </div>
            <br>
            <span>Enable issue progressing:</span>
            <props:checkboxProperty name="enableIssueProgressing"/>
            <br>
            <div id="jira.workflow">
                <props:multilineProperty name="jiraWorkflow" rows="5" cols="58" linkTitle="Enter your JIRA workflow for issue progressing"/>
            </div>
            <br>
            <span>Enable SSL connection:</span>
            <props:checkboxProperty name="enableSSLConnection"/>
            <br>
            <props:multilineProperty name="templateComment" rows="5" cols="58" linkTitle="Template comment for JIRA:"/>
        </td>
    </tr>
</l:settingsGroup>
<l:settingsGroup title="TeamCity Configuration">
    <tr>
        <td>
            <span>Server URL:</span>
            <br>
            <props:textProperty name="tcServerUrl" id="tcServerUrl" className="longField"/>
            <br>
            <span>User:</span>
            <br>
            <props:textProperty name="tcUser" id="tcUser"/>
            <br>
            <span>Password:</span>
            <br>
            <props:passwordProperty name="tcPassword"/>
        </td>
    </tr>
</l:settingsGroup>

<script type="text/javascript">
    var checkBox = jQuery('#enableIssueProgressing');
    checkBox.change(function(){
        if(jQuery(this).prop("checked")){
            BS.Util.show('jira.workflow');
        }
        else{
            BS.Util.hide('jira.workflow');
        }
        BS.VisibilityHandlers.updateVisibility('mainContent');
    });
    if(checkBox.prop("checked")){
        BS.Util.show('jira.workflow');
    }
    else{
        BS.Util.hide('jira.workflow');
    }
    var select = jQuery('[name="prop:issueIdPlace"]');
    select.change(function(){
        switch (select.val()){
            case 'custom':
                BS.Util.show('jira.issue.id');
                break;
            case 'teamcity':
                BS.Util.hide('jira.issue.id');
                break;
        }
    });
    switch (select.val()){
        case 'custom':
            BS.Util.show('jira.issue.id');
            break;
        case 'teamcity':
            BS.Util.hide('jira.issue.id');
            break;
    }
    BS.VisibilityHandlers.updateVisibility('mainContent');
</script>