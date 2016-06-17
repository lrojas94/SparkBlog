<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SparkBlog</title>
    <link rel="stylesheet" href="/bower/bootstrap/dist/css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="/bower/font-awesome/css/font-awesome.min.css" type="text/css">
    <link rel="stylesheet" href="/bower/datatables.net-bs/css/dataTables.bootstrap.min.css" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="/css/custom.css" type="text/css">
</head>
<body>
<div class="container-fluid" id="header-container">
    <#if (logged_in?? && !logged_in) || !(logged_in??)>
    <button id="login_btn" class="btn btn-primary" data-toggle="modal" data-target="#login_modal">Iniciar Sesion</button>
    <#else>
        <div id="login_status" data-user-id="${user.getId()}" data-name="${user.getName()}" class="col-xs-2">
            <a href="/user/${user.getId()}">Saludos ${user.getName()} <i class="fa fa-user"></i></a>
            |
             <a href="/logout">Logout</a>
        </div>
    </#if>
        <div class="jumbotron" id="header">
        <h1>SparkBlog</h1>
    </div>
</div>
<#include "./navbar.ftl">
<#if message_type??>
<br>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <!-- ALERTS -->
            <div class="alert alert-${message_type}"">
            	<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            	<strong>${message}</strong>
            </div>
        </div>
    </div>
</div>
</#if>
<#if template_name??>
    <#include template_name>
</#if>
<div class="container-fluid " id="footer-container">
    <div class="jumbotron" id="footer">
        <h4>Luis E. Rojas & Manuel E. Urena</h4>
    </div>
</div>
<#include "./login_modal.ftl">
<script src="/bower/jquery/dist/jquery.min.js"></script>
<script src="/bower/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="/bower/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="/bower/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="/js/custom.js"></script>
<script src="/js/websocket.js"></script>
</body>
</html>