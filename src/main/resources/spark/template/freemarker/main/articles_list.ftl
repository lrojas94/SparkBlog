<!-- FOREACH ARTICLE IN ARTICLES -->
<#import "../macros/articles.ftl" as Articles>
<#if articles??>
    <br>
    <table id='article-table' class="table table-responsive table-hover">
        <thead><th></th></thead>
        <tbody>
        </tbody>

    </table>
</#if>

<div class="article-container"  id="article-container-template" hidden>
    <div class="row">
        <div class="col-xs-12">
            <a href="/article/view/" class="article-view" >
                <h3 class="article-title"></h3>
            </a>
            <p class="article-preview"></p>
        </div>
        <div class="col-xs-8">
            <p><b>Escrito por</b>:
                <a href="/user/" class="article-author"> <i class="fa fa-user"></i></a></p>
        </div>
        <div class="col-xs-4 text-right">
            <a href="/article/view/" class="article-read-more" >Leer mas</a>
        </div>
        <div class="col-xs-12 article-tags">
            <p>
                Tags <i class="fa fa-tags"></i>:

            </p>
        </div>
    </div>
</div>

<a href="/tags/" class="tag-link" id="tag-template" hidden></a>