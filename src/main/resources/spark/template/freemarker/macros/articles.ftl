<#macro article_list article> <!-- Used to list article sin index site -->
<div class="article-container">
    <div class="row">
        <div class="col-xs-12">
            <h3 class="article-title">${article.getTitle()}</h3>
            <#assign articleBody = article.getBody()>
            <#if articleBody?length &gt; 70>
                <#assign maxLength = 70>
            <#else>
                <#assign maxLength = articleBody?length>
             </#if>
            <p class="article-preview">${articleBody?substring(0, maxLength)}</p>
        </div>
        <div class="col-xs-8">
            <p><b>Escrito por</b>: <a href="/users/${article.getAuthor()}">${article.getAuthor()} <i class="fa fa-user"></i></a></p>
        </div>
        <div class="col-xs-4 text-right">
            <a href="/articles/${article.getId()}" >Leer mas</a>
        </div>
        <div class="col-xs-12 article-tags">
            <p>
                Tags <i class="fa fa-tags"></i>:
                <#assign Tags = article.getTags()>
                <#list Tags as tag>
                    <a href="/tags/${tag.getId()}">${tag.getDescription()}</a>
                </#list>
            </p>
        </div>
    </div>
</div>
</#macro>
