<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SparkBlog</title>
    <link rel="stylesheet" href="/bower/bootstrap/dist/css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="/bower/font-awesome/css/font-awesome.min.css" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="/css/custom.css" type="text/css">
</head>
<body>
<div class="container-fluid" id="header-container">
    <#if (is_logged?? && !is_logged) || !(is_logged??)>
    <button id="login_btn" class="btn btn-primary" data-toggle="modal" data-target="#login_modal">Iniciar Sesion</button>
    <#else>
        <div id="login_status" class="col-xs-2">
            <span>Saludos ${user.getName()} <i class="fa fa-user"></i></span>
        </div>
    </#if>
        <div class="jumbotron" id="header">
        <h1>SparkBlog</h1>
    </div>
</div>
<#include "./navbar.ftl">
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
</body>
</html>