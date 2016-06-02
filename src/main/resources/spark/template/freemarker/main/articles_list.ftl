<!-- FOREACH ARTICLE IN ARTICLES -->
<#import "../macros/articles.ftl" as Articles>
<#if articles??>
    <#list articles as article>
        <@Articles.article_list article=article></@Articles.article_list>
    </#list>
</#if>
