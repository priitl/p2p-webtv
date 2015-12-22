(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('SessionsController', SessionsController);

  function SessionsController(Sessions, Principal) {
    var vm = this;

    vm.invalidate = invalidate;
    vm.sessions = {};

    activate();

    ////////////////

    function activate() {
      Principal.identity().then(function (account) {
        vm.account = account;
      });
      vm.success = null;
      vm.error = null;
      vm.sessions = Sessions.getAll();
    }

    function invalidate(series) {
      Sessions.delete({series: encodeURIComponent(series)},
        function () {
          vm.error = null;
          vm.success = 'OK';
          vm.sessions = Sessions.getAll();
        },
        function () {
          vm.success = null;
          vm.error = 'ERROR';
        });
    }
  }

})();

