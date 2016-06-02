/**
 * Created by luis on 6/2/16.
 */
var Users = function(){ //<-- this is basically a namespace.
    //Init data tables for user related stuff:
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
    }

    $('#show-user-articles').dataTable({
        fixedHeader : {
            header: true
        },
        language : languageForDataTable,
        lengthMenu : [5,10,25,50],
        pageLength : 5
    });

    return {}; //<-- returns an object with all public functions contained in users.
};

$(function(){
    var UserNamespace = Users();
});