(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('Tracker', Tracker);

  function Tracker($rootScope, $cookies, $http, $q) {
    var stompClient = null;
    var subscriber = null;
    var listener = $q.defer();
    var connected = $q.defer();
    var alreadyConnectedOnce = false;

    return {
      connect: connect,
      disconnect: disconnect,
      receive: receive,
      sendActivity: sendActivity,
      subscribe: subscribe,
      unsubscribe: unsubscribe
    };

    ////////////////

    function connect() {
      var loc = window.location;
      var url = '//' + loc.host + loc.pathname + 'websocket/tracker';
      var socket = new SockJS(url);
      stompClient = Stomp.over(socket);
      var headers = {};
      headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
      stompClient.connect(headers, function () {
        connected.resolve("success");
        sendActivity();
        if (!alreadyConnectedOnce) {
          $rootScope.$on('$stateChangeStart', function () {
            sendActivity();
          });
          alreadyConnectedOnce = true;
        }
      });
    }

    function subscribe() {
      connected.promise.then(function () {
        subscriber = stompClient.subscribe("/topic/tracker", function (data) {
          listener.notify(JSON.parse(data.body));
        });
      }, null, null);
    }

    function unsubscribe() {
      if (subscriber != null) {
        subscriber.unsubscribe();
      }
    }

    function receive() {
      return listener.promise;
    }

    function sendActivity() {
      if (stompClient != null && stompClient.connected) {
        stompClient.send('/topic/activity', {}, JSON.stringify({'page': $rootScope.toState.name}));
      }
    }

    function disconnect() {
      if (stompClient != null) {
        stompClient.disconnect();
        stompClient = null;
      }
    }
  }

})();
