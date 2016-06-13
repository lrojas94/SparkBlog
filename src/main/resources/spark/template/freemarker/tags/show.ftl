<div class="container">
    <div class="row">
        <!-- ARTICULOS -->
        <div id="tag-id" hidden>${tag.getId()}</div>
        <div class="col-xs-12">
            <div class="pop-out">
                <div class="col-xs-12 title">
                    <h2>Articulos</h2>
                    <h3>Relacionados a ${tag.getDescription()}</h3>
                </div>
                <div class="col-xs-12">
                <#include "../main/articles_list.ftl">
                </div>
            </div>
        </div>
    </div>
</div>