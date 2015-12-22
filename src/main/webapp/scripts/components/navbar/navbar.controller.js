(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('NavbarController', NavbarController);

  function NavbarController($rootScope, $state, Auth, Principal, ENV) {
    var vm = this;

    vm.account = {};
    vm.isAccountLoaded = false;
    vm.loadAccount = loadAccount;
    vm.logout = logout;

    activate();

    ////////////////

    function activate() {
      vm.state = $state;
      vm.inProduction = ENV === 'prod';
      vm.isAuthenticated = Principal.isAuthenticated;
    }

    $rootScope.$on('::userUpdated', function (event, data) {
      _.extend(vm.account, data);
      vm.isAccountLoaded = true;
    });

    $rootScope.$on('::userChanged', function (event, data) {
      _.extend(vm.account, data);
      vm.isAdmin = data != null && _.contains(data.authorities, 'ROLE_ADMIN');
      vm.isAccountLoaded = true;
    });

    function loadAccount() {
      Principal.identity().then(function (account) {
        vm.account = account;
        vm.isAdmin = account != null && _.contains(account.authorities, 'ROLE_ADMIN');
        vm.isAccountLoaded = true;
      });
    }

    function logout() {
      Auth.logout();
      $state.go('home');
    }
  }

})();
