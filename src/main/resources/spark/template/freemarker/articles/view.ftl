<div class="container pop-out">
    <div class="row article-view-header">
        <div class="col-xs-12">
            <h2>${article.getTitle()}</h2>
        </div>
    </div>
    <div class="row article-view-body">
        <div class="col-xs-12">
            <p>${article.getBody()}</p>
            <p>
                <i class="fa fa-tags"></i> Tags:
                <#assign ArticleTags = article.getArticleTags()>
                <#list ArticleTags as articleTag>
                    <a href="/tags/${articleTag.getTag().getId()}">#${articleTag.getTag().getDescription()}</a>
                </#list>
            </p>
        </div>
    </div>
    <!-- COMMENTS FORM -->
    <div class="row">
        <div class="col-xs-12">
            <form action="/comment/add" method="post" id="add-comment-form" role="form">
            	<legend>Deja un comentario!</legend>
                    <input type="hidden" value="${article.getId()}" name="articleId">
            	<div class="form-group">
            		<label for="comment-input">Comentario</label>
            		<input type="text" class="form-control" name="comment" id="comment-input"
                           placeholder="Commentario" required>
            	</div>
            	<button type="submit" class="btn btn-primary pull-right">Enviar comentario</button>
                <br>
            </form>
        </div>
    </div>

    <#if article.getComments()??>
        <#assign Comments = article.getComments()>
        <div class="row">
            <div class="col-xs-12">
                <table id="article-comment-table" class="table table-responsive table-striped">
                    <thead>
                        <th>Comentarios</th>
                        <th hidden>Ids</th>
                    </thead>
                    <tbody>
                        <#list Comments as comment>
                            <#assign author = comment.getAuthor()>
                            <tr>
                                <td>
                                    <a href="/user/${author.getId()}"><h4>${author}</h4></a>
                                    <p>${comment.getDescription()}</p>
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