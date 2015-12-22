(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('UserManagementDetailController', UserManagementDetailController);

  function UserManagementDetailController($stateParams, User) {
    var vm = this;

    vm.load = load;
    vm.user = {};

    activate();

    ////////////////

    function activate() {
      load($stateParams.login);
    }

    function load(login) {
      User.get({login: login}, function (result) {
        vm.user = result;
      });
    }
  }

})();

