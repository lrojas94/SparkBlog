<div class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-4 col-md-3">
            <!-- AVAILABLE CHATS -->
            <div class="row">
                <div id="admin-available-chats">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Chats disponibles
                        </div>
                        <div class="panel-body">
                            <ul class="list-group" id="admin-available-chats-users">
                            </ul>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="col-xs-12 col-sm-8 col-md-9">
            <!-- CHAT BOX -->
            <div class="row">
                <div class="col-xs-12" id="admin-message-box">
                    <ul id="admin-chat-msgs">
                    </ul>
                </div>
                <div class="col-xs-12">
                    <div class="input-group">
                        <input type="text" name="admin-chatMsg" id="admin-chatMsg" class="form-control " disabled value="" title="" required="required" >
                        <span class="input-group-btn">
                            <button  class="btn btn-default disabled" id="admin-chatSendMsg" type="button">Enviar</button>
                        </span>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<div id="admin-chat-message-template" hidden>
    <li>
        <small class="text-primary chatUsername"></small>
        <br>
    </li>
</div>

<div id="admin-chat-user-template" hidden>
    <li class="list-group-item admin-user-tab"><div class="badge"></div></li>
</div>