<%@ page errorPage="../../../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.exportuserpreferences.web.KeyJspBean"%>

<jsp:useBean id="key" scope="session" class="fr.paris.lutece.plugins.exportuserpreferences.web.KeyJspBean" />
<% 
    key.init( request, KeyJspBean.RIGHT_MANAGEEXPORTUSERPREFERENCES );
	response.sendRedirect( key.doExportCSV( request, response ) );
%>
