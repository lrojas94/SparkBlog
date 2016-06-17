<#import "../macros/chat.ftl" as Chat>
<div class="container pop-out">
    <div class="row article-view-header">
        <div class="col-xs-12">
            <h2>${article.getTitle()}</h2>
        </div>
    </div>
    <div class="row article-view-body" data-article-id="${article.getId()}">
        <div class="col-xs-12">
            <p>${article.getBody()}</p>
            <p>
                <i class="fa fa-tags"></i> Tags:
                <#assign ArticleTags = article.getTags()>
                <#list ArticleTags as articleTag>
                    <a href="/tags/${articleTag.getId()}">#${articleTag.getDescription()}</a>
                </#list>
            </p>
        </div>
    </div>
    <!-- COMMENTS FORM -->
    <#if user??>
        <div class="row pull-right">
            <div class="col-xs-12">
                <a href="#" id="like-article"
                   class=" btn btn-default <#if user.likes(article)>disabled</#if>">
                    <p class="text-center text-primary"><i class="fa fa-thumbs-up"></i>
                        <br>
                    <span class="count">${article.getLikes()!"0"}</span>
                    </p>
                </a>
                <a href="#" id="dislike-article"
                   class=" btn btn-default <#if user.dislikes(article)>disabled</#if>">
                    <p class="text-center text-primary"><i class="fa fa-thumbs-down"></i>
                        <br>
                    <span class="count">${article.getDislikes()!"0"}</span>
                    </p>
                </a>
                <a href="#" id="neutral-article"
                   class=" btn btn-default <#if !user.hasPreference(article)>disabled</#if>">
                    <p class="text-center text-primary"><i class="fa fa-meh-o"></i>
                        <br>
                        <span class="count">-</span>
                    </p>
                </a>
            </div>
        </div>
        <div class="row ">
            <div class="col-xs-12">
                <form action="/comment/add" method="post" id="add-comment-form" role="form">
                    <legend>Deja un comentario!</legend>
                    <div id="add-comment-errors"></div>
                    <input type="hidden" value="${article.getId()}" name="articleId">
                    <div class="form-group">
                        <label for="comment-input">Comentario</label>
                        <input type="text" class="form-control" name="comment" id="comment-input"
                               placeholder="Minimo 10 caracteres..." required>
                    </div>
                    <button type="submit" class="btn btn-primary pull-right">Enviar comentario</button>
                    <br>
                </form>
            </div>
        </div>
    </#if>

    <#if article.getComments()??>
        <#assign Comments = article.getComments()>
        <div class="row">
            <div class="col-xs-12" style="overflow: auto">
                <table id="article-comment-table" data-user-admin="${(user?? && (user.getAdministrator() || article.getAuthor().getId() = user.getId()))?string }" class="table table-responsive table-striped">
                    <thead>
                        <th>Comentarios</th>
                        <th hidden>Ids</th>
                    </thead>
                    <tbody>
                        <#list Comments as comment>
                            <#assign author = comment.getAuthor()>
                            <tr>
                                <td >
                                    <a href="/user/${author.getId()}"><h4>${author}</h4></a>
                                    <p class="comment-comment" data-id="${comment.getId()}">
                                    ${comment.getDescription()}
                                    </p>
                                    <#if user??>
                                        <p>
                                            <a href="#" class="btn btn-link like-comment <#if user.likes(comment)>disabled</#if>">
                                            <span class="count">${comment.getLikes()}</span>
                                                <i class="fa fa-thumbs-up"></i>
                                            </a>
                                            <a href="#" class="btn btn-link dislike-comment <#if user.dislikes(comment)>disabled</#if>">
                                            <span class="count">${comment.getDislikes()}</span>
                                                <i class="fa fa-thumbs-down"></i>
                                            </a>
                                            <a href="#" class="btn btn-link neutral-comment <#if !user.hasPreference(comment)>disabled</#if>">
                                                <i class="fa fa-meh-o"></i>
                                            </a>
                                            <#if user?? && (article.getAuthor() = user || user.getAdministrator())>
                                                <a href="/comment/delete/${comment.getId()}" class="delete-comment pull-right">
                                                    <i class="fa fa-exclamation-triangle"></i> Eliminar
                                                </a>
                                            </#if>
                                        </p>
                                    </#if>
                                </td>
                                <td hidden>${comment.getId()}</td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </#if>
</div>

<div id="comment-template" hidden>
    <a href="/user/" class="comment-author"><h4></h4></a>
    <p class="comment-comment" data-id="-1">
        <!-- COMMENT ITSELF -->
    </p>
    <p class="needsUser">
        <a href="#" class="btn btn-link like-comment ">
            <span class="count">0</span>
            <i class="fa fa-thumbs-up"></i>
        </a>
        <a href="#" class="btn btn-link dislike-comment">
            <span class="count">0</span>
            <i class="fa fa-thumbs-down"></i>
        </a>
        <a href="#" class="btn btn-link neutral-comment disabled">
            <i class="fa fa-meh-o"></i>
        </a>
        <span class="comment-delete-link">
        <a href="/comment/delete/" class="delete-comment pull-right">
        <i class="fa fa-exclamation-triangle"></i> Eliminar
        </a>
    </span>
    </p>
</div>


<@Chat.chatBox></@Chat.chatBox>