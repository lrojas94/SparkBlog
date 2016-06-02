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
                            <td><a href="/article/${article.getId()}">${article.getTitle()}</a></td>
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
    </#if>
</div>