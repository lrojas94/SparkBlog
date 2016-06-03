<div class="container">
    <div class="row">
        <!-- ARTICULOS -->
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

            <!-- PAGINATION -->
            <nav>
                <ul class="pagination pull-right">
                    <li>
                        <a href="#" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li class="active"><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li>
                        <a href="#" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>