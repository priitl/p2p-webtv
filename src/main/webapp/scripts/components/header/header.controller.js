(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('HeaderController', HeaderController);

  function HeaderController($scope, $state, Auth, Principal) {

    $scope.isAuthenticated = Principal.isAuthenticated;

    $scope.logout = function () {
      Auth.logout();
      $state.go('home');
    };

    $scope.searchTv = function () {
      if ($scope.search) {
        $state.go('media-search', {'title': $scope.search});
        $scope.search = undefined;
      }
    };
  }

})();
