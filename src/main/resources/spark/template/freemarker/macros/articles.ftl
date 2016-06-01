<#macro article_list article> <!-- Used to list article sin index site -->
<div class="article-container">
    <div class="row">
        <div class="col-xs-12">
            <h3 class="article-title">${article.getTitle()}</h3>
            <p class="article-preview">${article.getBody()[0..<70]}</p>
        </div>
        <div class="col-xs-8">
            <p><b>Escrito por</b>: <a href="/users/${article.getUsername()}">${article.getUsername()} <i class="fa fa-user"></i></a></p>
        </div>
        <div class="col-xs-4 text-right">
            <a href="/articles/${article.getId()}" >Leer mas</a>
        </div>
        <div class="col-xs-12 article-tags">
            <p>
                Tags <i class="fa fa-tags"></i>:
                <#list articles.getTags() as tag>
                    <a href="/tags/${tag.getId()}">${tag.getDescription()}</a>
                </#list>
            </p>
        </div>
    </div>
</div>
</#macro>
