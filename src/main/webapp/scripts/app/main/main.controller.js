(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('MainController', MainController);

  function MainController(Principal) {
    var vm = this;

    activate();

    ////////////////

    function activate() {
      Principal.identity().then(function (account) {
        vm.account = account;
        vm.isAuthenticated = Principal.isAuthenticated;
      });
      // TODO - remove later
      var client = new WebTorrent();
      var magnetUri = 'magnet:?xt=urn:btih:6a9759bffd5c0af65319979fb7832189f4f3c35d';

      client.add(magnetUri, function (torrent) {
        var file = torrent.files[0];
        file.appendTo('#player');
      })
    }
  }

})();

