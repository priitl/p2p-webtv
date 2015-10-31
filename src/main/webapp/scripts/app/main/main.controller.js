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
    }
  }

})();

