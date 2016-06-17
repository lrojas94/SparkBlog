<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">SparkBlog</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="/"><i class="fa fa-home"></i> Inicio <span class="sr-only">(current)</span></a></li>
                <#if user??>
                    <li><a href="/user/${user.getId()}"><i class="fa fa-user"></i> Perfil</a></li>
                </#if>
                <#if user?? && user.getAuthor()>
                    <li><a href="/article/add"><i class="fa fa-pencil"></i> Agregar Articulo</a></li>
                </#if>
                <#if user?? && (user.getAdministrator() || user.getAuthor())>
                    <li><a href="/chat"><i class="fa fa-comment"></i> Chat</a></li>
                </#if>
                <#if user?? && user.getAdministrator()>
                    <li><a href="/admin"><i class="fa fa-users"></i> Sitio Admin</a></li>
                </#if>

            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>