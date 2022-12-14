'use strict';


var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var btnChat = document.querySelector('#btn-chat');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var roomIdDisplay = document.querySelector('#room-id-display');
var messageForm = document.querySelector('#messageForm');

var stompClient = null;
var currentSubscription;
var usernamechat= null;
var roomId = null;
var topic = null;
var userchat = {
  username : null,
  imgsrc : null
};


function choseChat(name,room,src){

   userchat = {
    username :name,
    imgsrc :src
  }
  
  roomId = room;

  connect();
 
}



var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {

 usernamechat = "admin";  
  if (usernamechat) {

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
  }
  const list = document.getElementById("messageArea");

  while (list.hasChildNodes()) {
    list.removeChild(list.firstChild);
  }
  
  // event.preventDefault();
}

// Leave the current room and enter a new one.
function enterRoom(newRoomId) {
  roomId = newRoomId;
  roomIdDisplay.textContent = userchat.username;
  topic = `/app/chat/${newRoomId}`;

  if (currentSubscription) {
    currentSubscription.unsubscribe();
  }
  currentSubscription = stompClient.subscribe(`/channel/${roomId}`, onMessageReceived);

  stompClient.send(`${topic}/addUser`,
    {},
    JSON.stringify({sender: usernamechat, type: 'JOIN'})
  );
}

function onConnected() {
  enterRoom(roomId);
  
}

function onError(error) {
  connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
  connectingElement.style.color = 'red';
}

function sendMessage(event) {
  var messageContent = messageInput.value.trim();
  if (messageContent.startsWith('/join ')) {
    var newRoomId = messageContent.substring('/join '.length);
    enterRoom(newRoomId);
  } else if (messageContent && stompClient) {
    var chatMessage = {
      sender: usernamechat,
      content: messageInput.value,
      type: 'CHAT'
    };
    stompClient.send(`${topic}/sendMessage`, {}, JSON.stringify(chatMessage));
  }
  messageInput.value = '';
  event.preventDefault();
}

function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);

  var messageElement = document.createElement('li');
  var divElement = document.createElement('div');
  if (message.type == 'JOIN') {
    // messageArea.innerHTML = '';
    messageElement.classList.add('event-message');
    message.content = message.sender + ' joined!';
  } else if (message.type == 'LEAVE') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' left!';
  } else {
    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);
    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    divElement.appendChild(usernameElement);

    var timeElement = document.createElement('strong');
    var timeText = document.createTextNode(getCurrentTime());
    timeElement.appendChild(timeText);
    divElement.appendChild(usernameElement);
  }
  var divText = document.createTextNode(getCurrentTime());
  divElement.appendChild(divText);
  messageElement.appendChild(divElement);

  var textElement = document.createElement('p');
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);
  divElement.appendChild(textElement);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
      hash = 31 * hash + messageSender.charCodeAt(i);
  }
  var index = Math.abs(hash % colors.length);
  return colors[index];
}

function getCurrentTime() {
  return new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
}

$(document).ready(function() {
    messageForm.addEventListener('submit', sendMessage, true);
  
 
});
