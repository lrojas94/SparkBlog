<!-- Modal -->
<div class="modal fade" id="login_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Iniciar Sesion</h4>
            </div>
            <form action="/login" class="form" method="post" class="form">
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label for="username" class="col-xs-12 control-label">Nombre de Usuario</label>
                            <div class="col-xs-12">
                                <input type="text" class="form-control" name='username' id="username" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="password" class="col-xs-12 control-label">Contrasena</label>
                            <div class="col-xs-12">
                                <input type="password" name='password' class="form-control" id="password"  required>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="row">
                        <div class="col-xs-6">
                            <a href="/signup" class="btn btn-info btn-block">Registrate</a>
                        </div>
                        <div class="col-xs-6">
                            <input type="submit" class="btn btn-primary btn-block" value="Iniciar Sesion" required/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>