<div class="container">
<#if errors??>
    <div class="row">
        <div class="col-xs-12">
            <div class="alert alert-danger">
                Errores:
                <ul>
                    <#list errors as error>
                        <li>${error}</li>
                    </#list>
                </ul>
            </div>
        </div>
    </div>
</#if>
    <div class="row">
        <div class="col-xs-12">
            <form action="/article/add" method="post" role="form">
                <legend>Creación de Nuevo Artículo</legend>

                <div class="row">
                    <div class="form-group col-xs-12 col-md-12">
                        <label for="article_title"><i class="fa fa-file"></i> Título del Artículo</label>
                        <input type="text" class="form-control"
                               value='${article_title!""}'
                               name="article_title" id="title" placeholder="" required>
                    </div>
                    <div class="form-group col-xs-12 col-md-12">
                        <label for="article_body"><i class="fa fa-pencil"></i> Cuerpo del Articulo</label>
                        <textarea name="article_body" rows="5" id="body" class="form-control" value='${article_body!""}' required></textarea>
                    </div>
                </div>

                <div class="row">
                    <div class="form-group col-xs-12 col-md-6">
                        <label for="article_tags"><i class="fa fa-tags"></i> Tags</label>
                        <input type="text" class="form-control"
                               name="article_tags" id="tags" placeholder="(i.e.: tag1, tag2, tag3)">
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12">
                        <button type="submit" class="btn btn-primary btn-block">Agregar</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>