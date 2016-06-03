<!-- FOREACH ARTICLE IN ARTICLES -->
<#import "../macros/articles.ftl" as Articles>
<#if articles??>
    <br>
    <table id='article-table' class="table table-responsive table-hover">
        <thead><th></th></thead>
        <tbody>
            <#list articles as article>
            <tr>
                <td>
                    <@Articles.article_list article=article></@Articles.article_list>
                </td>
            </tr>
            </#list>
        </tbody>

    </table>
</#if>
