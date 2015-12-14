(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('HeaderController', HeaderController);

  function HeaderController($scope, $state, Auth, Principal) {

    $scope.isAuthenticated = Principal.isAuthenticated;

    $scope.logout = function () {
      Auth.logout();
      $state.go('home');
    }
  }

})();

