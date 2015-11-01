(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('NavbarController', NavbarController);

  function NavbarController($rootScope, $state, Auth, Principal, ENV) {
    var vm = this;

    vm.account = {};
    vm.logout = logout;

    activate();

    ////////////////

    function activate() {
      vm.state = $state;
      vm.inProduction = ENV === 'prod';
      Principal.identity().then(function (account) {
        vm.account = account;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.isAdmin = account != null && _.contains(account.authorities, 'ROLE_ADMIN');
      });
    }

    $rootScope.$on('::userUpdated', function (event, data) {
      _.extend(vm.account, data);
    });

    $rootScope.$on('::userChanged', function (event, data) {
      _.extend(vm.account, data);
      vm.isAdmin = data != null && _.contains(data.authorities, 'ROLE_ADMIN');
    });

    function logout() {
      Auth.logout();
      $state.go('home');
    }
  }

})();
