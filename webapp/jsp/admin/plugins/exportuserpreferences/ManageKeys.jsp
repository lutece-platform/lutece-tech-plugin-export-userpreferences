<jsp:useBean id="manageexportuserpreferencesKey" scope="session" class="fr.paris.lutece.plugins.exportuserpreferences.web.KeyJspBean" />
<% String strContent = manageexportuserpreferencesKey.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
