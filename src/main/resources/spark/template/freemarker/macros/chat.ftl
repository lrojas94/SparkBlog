<#macro chatBox> <!-- Used to list article sin index site -->
<div id="chat">
    <div id="chat-box" class="pop-out collapse">
        <div id="chat-inactive">
            <div class="form-group">
                <label for="chatUsername"><i class="fa fa-user"></i> Nombre de usuario de chat</label>
                <input type="text"  id="chatUsername" class="form-control" value="" title="" placeholder="Escriba aqui su nombre" required="required" >
            </div>
            <button class="btn btn-default btn-block" id="chatInit">Iniciar Chat</button>
        </div>
        <div id="chat-active" style="display: none;">
            <ul id="chat-msgs">
            </ul>
            <div class="input-group">
                <input type="text" name="chatMsg" id="chatMsg" class="form-control" value="" title="" required="required" >
                <span class="input-group-btn">
                    <button for='chatMsg' class="btn btn-default" id="chatSendMsg" type="button">Enviar</button>
                </span>
            </div>
        </div>
    </div>
    <button class="btn btn-primary btn-block" id="btn-chat" data-toggle="collapse" data-target="#chat-box">Chat</button>
</div>

<div id="chatMessageTemplate" hidden>
    <li>
        <small class="chatUsername text-primary"></small>
        <br>
    </li>
</div>
</#macro>