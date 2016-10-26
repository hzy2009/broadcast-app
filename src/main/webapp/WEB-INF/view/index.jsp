<%@page import="com.mcoding.broadcast.controller.BroadCastController"%>
<html>
<body>
<% if(BroadCastController.isVerify){
%>
<h2>启动成功</h2>
<%
}else{
%>
<h2>授权失败:<%=BroadCastController.verifyMsg %></h2>
<%	
} %>

</body>
</html>
