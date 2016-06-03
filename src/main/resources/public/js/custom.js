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
        lengthMenu : "Mostrar _MENU_ Entradas"
    };

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

    return {}; //<-- returns an object with all public functions contained in users.
};

var Comments = function(){
    var HelpersNamespace = Helpers();
    $("#article-comment-table").dataTable({
        language: HelpersNamespace.DTLanguage,
        pageLength: 5,
        lengthMenu: [],
        ordering: false,
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
                    console.log(Comment)
                    var comment = '<a href="/user/'+Comment.author.id+'"><h4>'
                        +Comment.author.username+'</h4></a>'+
                        '<p>'+Comment.description+'</p>';
                    $('#article-comment-table').DataTable().row.add([comment]).draw();
                }
            }
        })
    });
};

$(function(){
    var UserNamespace = Users();
    var CommentNamespace = Comments();
});