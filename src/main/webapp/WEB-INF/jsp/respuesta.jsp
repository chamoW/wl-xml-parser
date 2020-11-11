<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">
<head>

<!-- Access the bootstrap Css like this, 
		Spring boot will handle the resource mapping automcatically -->
<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

<!-- 
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />

</head>
<body>


	<div class="container-xl">

		<div class="starter-template">
			<h1>Revisión de GTLC</h1>
			<div>
				Archivo:<b> ${nombreArchivo}</b>
			</div>
			<div>
				Información extra:<b> ${extras}</b>
			</div>
			<br>
			<div>
				Respuesta:<br> ${respuesta}
			</div>
			<br>
			<c:if test="${fn:length(error) gt 0}">
				<div class="error-container">${error}</div>
			</c:if>

			<c:if test="${fn:length(exito) gt 0}">
				<div class="exito-container">${exito}</div>
			</c:if>


			<br> <br>

			<button class="btn btn-default"
				onclick="window.location.href='/'; return (false);">Nuevo</button>
		</div>

	</div>

	<script type="text/javascript"
		src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>

</body>

</html>
