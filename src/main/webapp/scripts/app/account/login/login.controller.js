(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('LoginController', LoginController);

  function LoginController($rootScope, $state, $timeout, $translate, Auth, toastr) {
    var vm = this;

    vm.login = login;

    activate();

    ////////////////

    function activate() {
      vm.user = {};
      vm.errors = {};
      vm.rememberMe = true;
      $timeout(function () {
        angular.element('[ng-model="vm.username"]').focus();
      });
    }

    function login(event) {
      event.preventDefault();
      Auth.login({
        username: vm.username,
        password: vm.password,
        rememberMe: vm.rememberMe
      }).then(function () {
        vm.authenticationError = false;
        if ($rootScope.previousStateName === 'register') {
          $state.go('home');
        } else {
          $rootScope.back();
        }
        toastr.success($translate.instant("login.messages.success", {username: vm.username}));
      }).catch(function () {
        vm.authenticationError = true;
      });
    }
  }

})();


