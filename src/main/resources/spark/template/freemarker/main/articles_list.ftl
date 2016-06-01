<!-- FOREACH ARTICLE IN ARTICLES -->
<div class="article-container">
    <div class="row">
        <div class="col-xs-12">
            <h3 class="article-title">Lorem Ipsum</h3>
            <p class="article-preview">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. In non nisi ipsum. Sed vulputate ante vitae dui iaculis, et consequat diam lobortis. In convallis, tortor consectetur tristique suscipit, lacus lectus lacinia justo, eget accumsan velit lacus sed urna. In ultrices mi in fermentum euismod. Suspendisse semper neque nec tristique rhoncus. Nam imperdiet malesuada varius. Sed in mauris neque. Proin interdum at neque et pulvinar. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Mauris non nisi nisi. Donec sit amet fermentum diam. Donec laoreet erat libero, vel tempus dolor pretium non. Praesent bibendum placerat felis vel hendrerit. Nam sodales ante eget luctus feugiat. Nam aliquam suscipit sem, a aliquam dolor maximus vitae. Sed justo est, volutpat sit amet rhoncus quis, rhoncus id lectus.
            </p>
        </div>
        <div class="col-xs-8">
            <p><b>Escrito por</b>: <a href="/ref-usuario">USUARIO <i class="fa fa-user"></i></a></p>
        </div>
        <div class="col-xs-4 text-right">
            <a href="/link-to-article" >Leer mas</a>
        </div>
        <div class="col-xs-12 article-tags">
            <p>Tags <i class="fa fa-tags"></i>: <a href="#">these</a> | <a href="#">are</a> | <a href="#">tags</a></p>
        </div>
    </div>
</div>
<#if articles??>
    <#list articles as article>
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
    </#list>
</#if>
