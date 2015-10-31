(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('UserManagementDetailController', UserManagementDetailController);

  function UserManagementDetailController($scope, $stateParams, User) {
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

