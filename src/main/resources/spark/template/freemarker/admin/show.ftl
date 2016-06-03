<!-- IMPORTANT
     We usually have an instance of {user} which is the currently logged in user.
     The user we're trying to visualize will be called {User} <- note caps.
 -->
<div class="container">
    <div class="row"> <!-- General Info -->
        <div class="col-xs-12">
            <h1>
            Gestion de Usuarios
            </h1>
            <h3>
            ${user.getName()} <small>(Administrador)</small>
            </h3>
        </div>
    </div> <!-- General Info Close -->
    <br>
    <table id='admin-user-table' class="table table-responsive table-hover">
        <thead>
            <th hidden>ID</th>
            <th>Nombre de Usuario</th>
            <th>Autor</th>
            <th>Administrador</th>
            <th>Opciones</th>
        </thead>
        <tbody>
            <#list users as User>
            <form action="/admin" method="post">
                <tr>
                    <td hidden><input type="hidden" name="userId" value="${User.getId()}"></td>
                    <td>${User.getUsername()}</td>
                    <td>
                        <input type="checkbox" name="author" <#if User.getAuthor()>checked</#if>>
                        </input>
                    </td>
                    <td>
                        <input type="checkbox" name="admin" <#if User.getAdministrator()>checked</#if>>
                        </input>
                    </td>
                    <td>
                        <div class="dropdown">
                        <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
                            Menu
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="/user/${User.getId()}">Ver</a></li>
                            <li role="presentation"><input type="submit" class='btn btn-link' value="Actualizar"/></li>
                            <li role="presentation"><a role="menuitem" tabindex="-1" href="/user/delete/${User.getId()}">Eliminar</a></li>
                        </ul>
                    </div>
                    </td>
                </tr>
            </form>

            </#list>
        </tbody>
    </table>
</div>