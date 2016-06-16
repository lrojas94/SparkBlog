/**
 * Created by MEUrena on 6/14/16.
 */
var userWebSocketHandler = {
    webSocket : null,
    prepareMessage : function(message){
        var data = {
          userInfo : userWebSocketHandler.userInfo,
          type: "MESSAGE",
          message: message,
          to: "ADMIN"
        };
        return data;

    },
    appendMessage : function (data) {
        var msgContainer = $('#chatMessageTemplate').clone();
        msgContainer.find('.chatUsername').append(data.userInfo.username);
        msgContainer.find('li').append(data.message);
        $('#chat-msgs').append(msgContainer.html());
    },
    sendMessage : function(jsonData){ //Utility function to receive data.
        jsonData = jsonData || userWebSocketHandler.preparedData;
        userWebSocketHandler.webSocket.send(JSON.stringify(jsonData));
    },
    msgReceived : function(msgEvent){ // This is gonna be private.
        var data = JSON.parse(msgEvent.data);
        switch(data.type){
            case "INIT":
                //Here we set up webSocket.userInfo. INIT messages happen only once.
                userWebSocketHandler.userInfo = data.userInfo;
                $('#chat-inactive').slideToggle(function(){
                    $("#chat-active").slideToggle();
                });
                break;
            case "MESSAGE":
                userWebSocketHandler.appendMessage(data);
                break
        }
    },
    onOpen : function(){
        //What would happen when connection is opened?
        console.log("Connectin opened!");

        var data = {
            type: "INIT",
            userInfo : {
                username: $('#chatUsername').val()
            }
        }; //This will be handled by the server, but we have to send a first message containing user info:
        userWebSocketHandler.sendMessage(data);
    },
    onClose : function(){
        console.log("Sadly closed :(");
    },
    init : function(){


        var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatRoom/");
        webSocket.onmessage = this.msgReceived;
        webSocket.onopen = this.onOpen;
        webSocket.onclose = this.onClose;

        this.webSocket = webSocket;
    },
    userInfo : {} //User information. Usually set after opened.
};

$(function(){
   //initialize all websocket behavior here:
    //
    $('#chatInit').click(function(e){
       userWebSocketHandler.init();
    });
    
    $('#chatSendMsg').click(function(e){
        var message = $('#chatMsg').val();
        $('#chatMsg').val("");
        var data = userWebSocketHandler.prepareMessage(message);
        userWebSocketHandler.sendMessage(data);
        userWebSocketHandler.appendMessage(data);
    })
});