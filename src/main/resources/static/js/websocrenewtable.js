/*сервер будет понгом pong*/
ws = new WebSocket("ws://localhost:8095/sbb/v2/pong");

ws.onopen = function(){
/*этот экшен будет наполнять div id="stack"*/
/*action('open connection');*/
console.log('WS : Connection opened');
};

/*к нам придёт сообщение от сервера для отправки во фронт*/
ws.onmessage = function(event){
action(event);
};

ws.onerror = function(event){
console.log('WS : Error occurred');
/*var divErrorMessage = document.getElementById('ws-error-message');*/
/*divErrorMessage.innerHTML = 'ERROR : Railway Timetable server was disconnected. Timetable will not be updated online!';*/
};
ws.onclose = function(event){
    console.log('WS : Connection closed');
/*    var divErrorMessage = document.getElementById('ws-error-message');*/
/*    divErrorMessage.innerHTML = 'WARNING : Railway Timetable server was disconnected. Timetable will not be updated online!';*/
};

/*функция экшн принимает сообщение и его надо отобразить в браузере - отдать во фронт, чтобы отпечатать в div id="stack"*/
function action(event){
    /*просто вывести сообщение на экран*/
    /*var output = document.getElementById("stack");
    var newP = document.createElement('p');
    newP.appendChild(document.createTextNode(event.data));
    output.appendChild(newP);*/

    /*обновлять таблицу*/
    console.log('WS : Message received event.data : ' + event.data);

    var jsonArrArrivals = JSON.parse(event.data);
    /*console.log('jsonArrArrivals = ' + jsonArrArrivals);*/

    var tableArrivals = document.getElementById('table-ws-timetable-arrival');
    // ERASE OLD INFO
    /*console.log('111');*/
        tableArrivals.innerHTML =
            '<tr>' +
            '<th>' + 'Номер поезда' + '</th>' +
            '<th>' + 'Откуда' + '</th>' +
            '<th>' + 'Прибытие' + '</th>' +
            '<th>' + 'Отправление' + '</th>' +
            '<th>' + 'Куда' + '</th>' +
            '</tr>'
        ;
// FILL NEW INFO - ARRIVALS
/*console.log('222');*/
      for (var i = 0; i < jsonArrArrivals.length; i++) {
      /*console.log('333');*/
          var string = jsonArrArrivals[i];
          var json = JSON.parse(string);
          /*console.log('444 json = ' + json);
          console.log('555 json.trainNumber = ' + json.trainNumber);*/

          tableArrivals.innerHTML +=
              '<tr>' +
              '<td>' + json.trainNumber + '</td>' +
              '<td>' + json.previousStationName + '</td>' +
              '<td>' + json.currentStationArrTime + '</td>' +
              '<td>' + json.currentStationDepTime + '</td>' +
              '<td>' + json.nextStationName + '</td>' +
              '</tr>'
          ;
      }
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
