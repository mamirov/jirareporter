<%@ include file="/include.jsp" %>

<c:set var="title" value="Test page" scope="request"/>

<jsp:useBean id="name" type="java.lang.String" scope="request"/>
<bs:page>
  <jsp:attribute name="body_include">
    <h1>Plugin ${name}</h1>
  </jsp:attribute>
</bs:page>
