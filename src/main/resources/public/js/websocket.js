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
        $('#chat-msgs').animate({
            scrollTop: $('#chat-msgs').scrollTop() + $('#chat-msgs li:last').position().top
        }, 200);
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
        console.log("Connection opened!");

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

    // When chat init is pressed
    $('#chatInit').click(function(e){
       userWebSocketHandler.init();
    });

    // When ENTER is pressed
    $('#chatUsername').keypress(function (e) {
        if (e.keyCode === 13) {
            userWebSocketHandler.init();
        }
    });

    // When send button is pressed
    $('#chatSendMsg').click(function(e){
        var message = $('#chatMsg').val();
        $('#chatMsg').val("");
        var data = userWebSocketHandler.prepareMessage(message);
        userWebSocketHandler.sendMessage(data);
        userWebSocketHandler.appendMessage(data);
    });

    // When ENTER is pressed
    $('#chatMsg').keypress(function (e) {
        if (e.keyCode === 13) {
            var message = $(this).val();
            $(this).val("");
            var data = userWebSocketHandler.prepareMessage(message);
            userWebSocketHandler.sendMessage(data);
            userWebSocketHandler.appendMessage(data);
        }
    });
});
