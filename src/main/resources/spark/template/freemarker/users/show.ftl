<#import '../macros/articles.ftl' as Articles>
<!-- IMPORTANT
     We usually have an instance of {user} which is the currently logged in user.
     The user we're trying to visualize will be called {User} <- note caps.
 -->
<div class="container">
    <div class="row"> <!-- General Info -->
        <div class="col-xs-12">
            <h1>
            ${User.getName()}
            <#if User.getAdministrator()>
                <small>(Administrador)</small>
            <#elseif User.getAuthor()>
                <small>(Autor)</small>
            </#if>
            </h1>
            <h3>${User.getUsername()}</h3>
        </div>
    </div> <!-- General Info Close -->
    <#if User.getAuthor() && articles??>
        <@Articles.show_user_article_table articles=articles></@Articles.show_user_article_table>
    </#if>
</div>