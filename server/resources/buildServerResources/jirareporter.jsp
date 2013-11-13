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
            <label for="jiraServerURL">Server URL:</label>
            <props:textProperty name="jiraServerUrl" id="jiraServerURL" className="longField"/>
            <label for="jiraUser">User:</label>
            <props:textProperty name="jiraUser" id="jiraUser"/>
            <label for="jiraPassword">Password</label>
            <props:textProperty name="jiraPassword" id="jiraPassword"/>
        </td>
    </tr>
</l:settingsGroup>