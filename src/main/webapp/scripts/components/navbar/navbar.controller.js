'use strict';

angular.module('maurusApp')
  .controller('NavbarController', function ($rootScope, $scope, $location, $state, Auth, Principal, ENV) {
                $scope.$state = $state;
                $scope.inProduction = ENV === 'prod';

                $rootScope.$on('::userUpdated', function (event, data) {
                  _.extend($scope.account, data);
                });

                Principal.identity().then(function (account) {
                  $scope.account = account;
                  $scope.isAuthenticated = Principal.isAuthenticated;
                });

                $scope.logout = function () {
                  Auth.logout();
                  $state.go('home');
                };
              });
