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
            <form action="/signup" method="post" role="form">
                <legend>Registro de Nuevo Usuario</legend>

                <div class="row">
                    <div class="form-group col-xs-12 col-md-6">
                        <label for="username"><i class="fa fa-user"></i> Nombre de Usuario</label>
                        <input type="text" class="form-control"
                               value='${username!""}'
                               name="username" id="username" placeholder="" required>
                    </div>
                    <div class="form-group col-xs-12 col-md-6">
                        <label for="fullname">Nombre Completo</label>
                        <input type="text" class="form-control"
                               value='${fullname!""}'
                               name="fullname" id="fullname" placeholder="">
                    </div>
                </div>

                <div class="row">
                    <div class="form-group col-xs-12 col-md-6">
                        <label for="password"><i class="fa fa-key"></i> Contrasena</label>
                        <input type="password" class="form-control"
                               name="password" id="password" placeholder="">
                    </div>
                    <div class="form-group col-xs-12 col-md-6">
                        <label for="password2">Repetir Contrasena</label>
                        <input type="password" class="form-control"
                               name="password2" id="password2" placeholder="">
                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-12">
                        <button type="submit" class="btn btn-primary btn-block">Registrarse</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>