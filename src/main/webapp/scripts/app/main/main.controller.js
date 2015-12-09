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
      var magnetUri = 'magnet:?xt=urn:btih:96580803dd0a924e5a97b3b24d18ea6006e9668b';

      client.add(magnetUri, function (torrent) {
        torrent.files.forEach(function (file) {
          file.appendTo('#player');
        })
      })
    }
  }

})();

