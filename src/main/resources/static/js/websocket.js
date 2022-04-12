/*сервер будет понгом pong*/
ws = new WebSocket("ws://localhost:8095/sbb/v2/pong");

ws.onopen = function(){
/*этот экшен будет наполнять div id="stack"*/
/*action('open connection');*/
};

/*к нам придёт сообщение от сервера для отправки во фронт*/
ws.onmessage = function(event){
action(event.data);
};

ws.onerror = function(event){};
ws.onclose = function(event){};

/*функция экшн принимает сообщение и его надо отобразить в браузере - отдать во фронт, чтобы отпечатать в div id="stack"*/
function action(message){
    var output = document.getElementById("stack");
    /*создадим новый параграф <p></p>*/
    var newP = document.createElement('p');
    newP.appendChild(document.createTextNode(message));
    output.appendChild(newP);
};

/*функция пинг передаёт текст от фронта на сервер*/
function ping(){
    var message = document.getElementById('message').value;
    /*просто для наглядности выведем в div id="stack"*/
    action('from browser was sent a text: ' + message);
    ws.send(message);
}


/*
window.onload = function(){
    ws = new WebSocket("ws://localhost:8095/sbb/v2/schedule_on_rwstation_handler");
    ws.onmessage = function(event) {
    document.getElementById('td_time').innerText = event.data;
    }
};*/
