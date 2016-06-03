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
            <p><b>Escrito por</b>: <a href="/user/${article.getAuthor().getId()}">${article.getAuthor()} <i class="fa fa-user"></i></a></p>
        </div>
        <div class="col-xs-4 text-right">
            <a href="/article/view/${article.getId()}" >Leer mas</a>
        </div>
        <#assign ArticleTags = article.getArticleTags()>
        <#if ArticleTags?size != 0>
            <div class="col-xs-12 article-tags">
                <p>
                    Tags <i class="fa fa-tags"></i>:
                    <#list ArticleTags as articleTag>
                        <a href="/tags/${articleTag.tag.getId()}">${articleTag.tag.getDescription()}</a>
                    </#list>
                </p>
            </div>
        </#if>
    </div>
</div>
</#macro>

<#macro show_user_article_table articles>
<div class="row"> <!-- Articles Written -->
    <div class="col-xs-12">
        <table class="table table-striped table-hover" id="show-user-articles">
            <thead>
            <th>Titulo del Articulo</th>
            <th>Fecha de Publicacion</th>
                <#if user?? && User.getId() = user.getId()>
                <th>Opciones</th>
                </#if>
            </thead>
            <tbody>
                <#list articles as article>
                <tr>
                    <td><a href="/article/view/${article.getId()}">${article.getTitle()}</a></td>
                    <td>${article.getDatePublished()?date}</td>
                    <#if user?? && User.getId() = user.getId()>
                        <td>
                            <div class="dropdown">
                                <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                                    Menu
                                    <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                                    <li role="presentation"><a role="menuitem" tabindex="-1" href="/article/${article.getId()}"><i class="fa fa-eye"></i> Ver</a></li>
                                    <li role="presentation"><a role="menuitem" tabindex="-1" href="/article/edit/${article.getId()}"><i class="fa fa-pencil"></i> Editar</a></li>
                                    <li role="presentation"><a role="menuitem" tabindex="-1" href="/article/delete/${article.getId()}"><i class="fa fa-exclamation-triangle"></i> Borrar</a></li>
                                </ul>
                            </div>
                        </td>
                    </#if>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
</#macro>