/**
 * Created by luis on 6/2/16.
 */
var Helpers = function(){
    var languageForDataTable = {
        search : "Buscar Entradas",
        paginate: {
            first:    'Inicio',
            previous: 'Anterior',
            next:     'Siguiente',
            last:     'Ultimo'
        },
        info : "Mostrando del _START_ al _END_ de un total de _TOTAL_ Entradas",
        lengthMenu : "Mostrar _MENU_ Entradas",
        emptyTable: "No hay datos a mostrar.",
        infoEmpty: ""
    };

    $('.submit-form').click(function (e) {
        e.preventDefault();
        var form = $(this).closest('form');
        console.log(form);
        form.submit();
    });

    var FormDataToJson = function(form){
        var formData = $(form).serializeArray();
        var jsonFormData = {};
        for(i in formData){
            jsonFormData[formData[i].name] = formData[i].value;
        }
        return jsonFormData;
    };

    return {
        FormDataToJson : FormDataToJson,
        DTLanguage: languageForDataTable
    };
};

var Users = function(){ //<-- this is basically a namespace.
    //Init data tables for user related stuff:
    var HelpersNamespace = Helpers();

    $('#show-user-articles').dataTable({
        fixedHeader : {
            header: true
        },
        language : HelpersNamespace.DTLanguage,
        lengthMenu : [5,10,25,50],
        pageLength : 5,
    });

    // $('#admin-user-table').dataTable({
    //     fixedHeader : {
    //         header: true
    //     },
    //     language : HelpersNamespace.DTLanguage,
    //     lengthMenu : [5,10,25,50],
    //     pageLength : 5,
    // });

    return {}; //<-- returns an object with all public functions contained in users.
};

var Comments = function(){
    var HelpersNamespace = Helpers();
    var initArticleCommentDelete = function(e){
        e.preventDefault();
        var tr = $(this).closest('tr');
        $.ajax({
            url: $(this).attr("href"),
            dataType: 'json',
            method: "POST",
            success: function(data){
                if(data.status === "success"){
                    //remove from row.
                    var table = $('#article-comment-table').DataTable();
                    table.row(tr).remove().draw(false);
                }
            }

        })
    };

    var setUserPreference = function(preference,elem){
        //Gather general info:

        elem.blur();

        var data = {
            isArticle : false,
            preferenceId: $(elem).closest('td').first().find('.comment-comment').data('id'), //This holds comments ids on tables.
            userId: $('#login_status').data('user-id'),
            preference: preference
        };

        var comment = $(elem).closest('td');

        $.ajax({
            url: "/comment/preference",
            method: "POST",
            data: JSON.stringify(data),
            dataType: 'json',
            success: function(data){
                //Set article likes/dislikes:
                var like = comment.find('.like-comment');
                var dislike = comment.find('.dislike-comment');
                var neutral = comment.find('.neutral-comment');
                like.find('.count').text(data.returnObject.likesCount);
                dislike.find('.count').text(data.returnObject.dislikesCount);

                switch(preference){
                    case "like":
                        //show other two:
                        dislike.removeClass("disabled");
                        neutral.removeClass("disabled");
                        like.addClass("disabled");
                        break;
                    case "dislike":
                        like.removeClass("disabled");
                        neutral.removeClass("disabled");
                        dislike.addClass("disabled");
                        break;
                    case "neutral":
                        dislike.removeClass("disabled");
                        like.removeClass("disabled");
                        neutral.addClass("disabled");
                        break;
                }
                console.log(data);
            }

        });
    };


    $("#article-comment-table").dataTable({
        language: HelpersNamespace.DTLanguage,
        pageLength: 5,
        lengthMenu: [],
        ordering: true,
        columnDefs:[
            { orderable : false, targets: 0},
            { visible: false, targets: 1}
        ],
        order: [[1,'desc']],
        searching: false,
        lengthChange: false
    });

    $('#add-comment-form').submit(function(e){
        e.preventDefault();
        //Send with json:
        var jsonFormData = HelpersNamespace.FormDataToJson(this);
        $.ajax({
            url: $(this).attr("action"),
            data: JSON.stringify(jsonFormData),
            method: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data){
                if(data.status === "success"){
                    //Adding it was successful
                    //Add to DT:
                    var Comment = data.returnObject;
                    var commentTemplate = $('#comment-template').clone().attr("id","");
                    var commentAuthor = $(commentTemplate).find(".comment-author");
                    commentAuthor.attr("href",commentAuthor.attr("href") + Comment.author.id);
                    commentAuthor.find("h4").text(Comment.author.username);
                    var commentSpace = $(commentTemplate).find('.comment-comment')[0];
                    $(commentSpace)[0].dataset.id = Comment.id.toString();
                    $(commentSpace).append(Comment.description);
                    var commentDelete = $(commentTemplate).find(".comment-delete-link");
                    if($('#article-comment-table').data('user-admin')){
                        var link = commentDelete.find('a');
                        link.attr("href",link.attr("href")+Comment.id);
                    }
                    else{
                        if($('#login_status').length != 0) {
                            //User is logged in
                            //Just remove delete:
                            $(commentDelete).remove();
                        }
                        else{
                            commentTemplate.find('.needsUser').remove();
                        }
                    }

                    $('#article-comment-table').DataTable().row.add([commentTemplate.html(),Comment.id.toString()]).sort().draw();
                    $('#add-comment-errors').html("");
                    $('#comment-input').val("");
                }
                else if(data.status === "error"){
                    var alert = $('<div>').
                                addClass("alert alert-danger").
                                append('<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>').
                                append(data.errors.join("\n"));

                    $('#add-comment-errors').html(alert);
                }
            }
        })
    });

    $('body').on({
        click : initArticleCommentDelete
    },'.delete-comment')
        .on({
            click: function(e){
                e.preventDefault();
                setUserPreference("like",this)
            }
        },'.like-comment')
        .on({
            click: function(e){
                e.preventDefault();
                setUserPreference("dislike",this)
            }
        },'.dislike-comment')
        .on({
            click: function(e){
                e.preventDefault();
                setUserPreference("neutral",this)
            }
        },'.neutral-comment');
};

var Articles = function(){
    var HelpersNamespace = Helpers();
    $('#article-table').dataTable({
        language: HelpersNamespace.DTLanguage,
        pageLength: 10,
        lengthMenu: [10,25,50],
        ordering: false
    });

    var setUserPreference = function(preference){
        //Gather general info:

        var data = {
            isArticle : true,
            preferenceId: $('.article-view-body').data('article-id'),
            userId: $('#login_status').data('user-id'),
            preference: preference
        };

        console.log(data);

        $.ajax({
            url: "/article/preference",
            method: "POST",
            data: JSON.stringify(data),
            dataType: 'json',
            success: function(data){
                //Set article likes/dislikes:
                $('#like-article .count').text(data.returnObject.likesCount);
                $('#dislike-article .count').text(data.returnObject.dislikesCount);

                switch(preference){
                    case "like":
                        //show other two:
                        $('#neutral-article,#dislike-article').removeClass("disabled");
                        $('#like-article').addClass("disabled");
                        break;
                    case "dislike":
                        $('#like-article,#neutral-article').removeClass("disabled");
                        $('#dislike-article').addClass("disabled");
                        break;
                    case "neutral":
                        $('#like-article,#dislike-article').removeClass("disabled");
                        $('#neutral-article').addClass("disabled");
                        break;
                }
                console.log(data);
            }

        });
    };

    $('#like-article').click(function(e){
        e.preventDefault();
        setUserPreference("like");
    });
    $('#dislike-article').click(function(e){
        e.preventDefault();
        setUserPreference("dislike");
    });
    $('#neutral-article').click(function(e){
        e.preventDefault();
        setUserPreference("neutral");
    });
};

$(function(){
    var UserNamespace = Users();
    var CommentNamespace = Comments();
    var ArticleNamespace = Articles();
    var HelpersNamespace = Helpers();
});