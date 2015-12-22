(function () {
  'use strict';

  angular
    .module('wtvApp')
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

