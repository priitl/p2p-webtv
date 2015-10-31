(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('PasswordController', PasswordController);

  function PasswordController(Auth, Principal) {
    var vm = this;

    vm.account = {};
    vm.changePassword = changePassword;

    activate();

    ////////////////

    function activate() {
      Principal.identity().then(function (account) {
        vm.account = account;
      });
      vm.success = null;
      vm.error = null;
      vm.doNotMatch = null;
    }

    function changePassword() {
      if (vm.password !== vm.confirmPassword) {
        vm.doNotMatch = 'ERROR';
      } else {
        vm.doNotMatch = null;
        Auth.changePassword(vm.password).then(function () {
          vm.error = null;
          vm.success = 'OK';
        }).catch(function () {
          vm.success = null;
          vm.error = 'ERROR';
        });
      }
    }
  }

})();


