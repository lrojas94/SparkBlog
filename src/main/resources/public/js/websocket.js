/**
 * Created by MEUrena on 6/14/16.
 */
var userWebSocketHandler = {
    webSocket : null,
    prepareMessage : function(message){
        var data = {
          userInfo : userWebSocketHandler.userInfo,
          type: "ADMIN_MESSAGE",
          message: message,
          to: ""
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
            case "USER_MESSAGE":
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

var adminWebSocketHandler = {
    webSocket : null,

    openChats: {},
    activeChat : null, //userInfo variable

    prepareMessage : function(message){
        if(adminWebSocketHandler.activeChat === null){
            return null;
        }
        var data = {
            userInfo : adminWebSocketHandler.userInfo,
            type: "USER_MESSAGE",
            message: message,
            to: adminWebSocketHandler.activeChat.username
        };
        return data;

    },
    appendMessage : function (data) {
        var msgContainer = $('#admin-chat-message-template').clone();
        msgContainer.find('.chatUsername').append(data.userInfo.username);
        msgContainer.find('li').append(data.message);
        msgContainer = $(msgContainer.html())

        if((adminWebSocketHandler.activeChat != null &&
            data.userInfo.username === adminWebSocketHandler.activeChat.username) ||
            data.userInfo.username === adminWebSocketHandler.userInfo.username){

            //ONLY if he's the current chat, append it.
            $('#admin-chat-msgs').append(msgContainer);
            $('#admin-chat-msgs').animate({
                scrollTop: $('#admin-chat-msgs').scrollTop() + $('#admin-chat-msgs li:last').position().top
            }, 200);
        }
        else if(data.userInfo.username !== adminWebSocketHandler.userInfo.username &&
            adminWebSocketHandler.openChats[data.userInfo.username] != null){
            //add to count list:
            var unread = ++adminWebSocketHandler.openChats[data.userInfo.username].unread;
            var $userTab = $(adminWebSocketHandler.openChats[data.userInfo.username].userTab);
            $userTab.find('.badge').text(unread);
        }
        else{

        }


        return msgContainer;
    },
    addUser : function(userInfo){

        var chat = userInfo;
        chat.messages = [];
        chat.unread = 0;

        var user = $('#admin-chat-user-template').clone();

        user.find('li').append(userInfo.username);

        chat.userTab = $(user.html());

        adminWebSocketHandler.openChats[userInfo.username] = chat;

        $('#admin-available-chats-users').append(adminWebSocketHandler.openChats[userInfo.username].userTab);
    },
    sendMessage : function(jsonData){ //Utility function to send data.
        //append if Im sending the user a messaage.
        if(jsonData.type === "USER_MESSAGE"){
            var msg = adminWebSocketHandler.appendMessage(jsonData);
            adminWebSocketHandler.activeChat.messages.push(msg);
        }
        adminWebSocketHandler.webSocket.send(JSON.stringify(jsonData));
    },
    msgReceived : function(msgEvent){ // This is gonna be private... Well, it should
        var data = JSON.parse(msgEvent.data);
        switch(data.type){
            case "INIT":
                //Here we set up webSocket.userInfo. INIT messages happen only once.
                console.log(data);
                adminWebSocketHandler.userInfo = data.userInfo;
                //Prepare Users
                if(data.onlineUsers !== undefined){
                    for(var i in data.onlineUsers) {
                        var userInfo = data.onlineUsers[i];
                        adminWebSocketHandler.addUser(userInfo);
                    }
                }

                console.log(adminWebSocketHandler.openChats);

                break;
            case "ADMIN_MESSAGE":
                var msg = adminWebSocketHandler.appendMessage(data);
                adminWebSocketHandler.openChats[data.userInfo.username].messages.push(msg);
                break;
            case "USER_JOINED":
                adminWebSocketHandler.addUser(data.userInfo);
                break;
            case "USER_LEFT":
                adminWebSocketHandler.openChats[data.userInfo.username].userTab.remove();
                adminWebSocketHandler.openChats[data.userInfo.username] = null;
                if(adminWebSocketHandler.activeChat.username === data.userInfo.username){
                    //remove all msgs:
                    $('#admin-chat-msgs').html('');
                    $('#admin-chatSendMsg').addClass('disabled');
                    $('#admin-chatMsg').attr("disabled",'true');
                    adminWebSocketHandler.activeChat = null;
                }
                break


        }
    },
    onOpen : function(){
        //What would happen when connection is opened?
        console.log("ADMIN Connection opened!");

        var data = {
            type: "INIT",
            userInfo : {
                username: '(admin) ' + $('#login_status').data('name'),
                admin: true
            }
        }; //This will be handled by the server, but we have to send a first message containing user info:
        adminWebSocketHandler.sendMessage(data);
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


        $('body').on({
            click : function(e) { adminWebSocketHandler.openChat(this); }
        },'.admin-user-tab');

        $('#admin-chatSendMsg').click(function(e){
            var messageString = $('#admin-chatMsg').val();
            $('#admin-chatMsg').val('');
            var data = adminWebSocketHandler.prepareMessage(messageString);
            adminWebSocketHandler.sendMessage(data);
        });

        $('#admin-chatMsg').keyup(function(e){
            if(e.keyCode == 13){
                $('#admin-chatSendMsg').click();
            }
        });
    },
    openChat : function(elem){
        $('.admin-user-tab').removeClass('active');
        $(elem).addClass('active');
        $(elem).find('.badge').text('');
        $('#admin-chatSendMsg').removeClass('disabled');
        $('#admin-chatMsg').removeAttr('disabled');
        adminWebSocketHandler.openChats[$(elem).text()].unread = 0;
        adminWebSocketHandler.activeChat = adminWebSocketHandler.openChats[$(elem).text()];
        //add chats:
        $('#admin-chat-msgs').html('');
        for(i in adminWebSocketHandler.activeChat.messages) {
            $('#admin-chat-msgs').append(adminWebSocketHandler.activeChat.messages[i]);
        }
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
            $('#chatInit').click();
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
            $('#chatSendMsg').click(); //Just trigger click event.
        }
    });

    if($('#login_status').length !== 0 && $('#admin-available-chats').length != 0){
        //acivate admin webchat
        adminWebSocketHandler.init();
    }
});
