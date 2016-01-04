<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageexportuserpreferences" scope="session" class="fr.paris.lutece.plugins.exportuserpreferences.web.ManageExportuserpreferencesJspBean" />

<% manageexportuserpreferences.init( request, manageexportuserpreferences.RIGHT_MANAGEEXPORTUSERPREFERENCES ); %>
<%= manageexportuserpreferences.getManageExportuserpreferencesHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
