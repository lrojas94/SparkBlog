/**
 * Created by MEUrena on 6/14/16.
 */
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatRoom/");

webSocket.onmessage = function (msg) { alert("Caught something: " + msg) };
webSocket.onclose = function () { alert("WebSocket connection closed") };