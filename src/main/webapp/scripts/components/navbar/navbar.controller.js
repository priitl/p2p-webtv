'use strict';

angular.module('maurusApp')
  .controller('NavbarController', function ($rootScope, $scope, $location, $state, Auth, Principal, ENV) {
                $scope.$state = $state;
                $scope.inProduction = ENV === 'prod';
                $scope.isAdmin = false;

                $rootScope.$on('::userUpdated', function (event, data) {
                  _.extend($scope.account, data);
                });

                $rootScope.$on('::userChanged', function (event, data) {
                  _.extend($scope.account, data);
                  $scope.isAdmin = data != null && _.contains(data.authorities, 'ROLE_ADMIN');
                });

                Principal.identity().then(function (account) {
                  $scope.account = account;
                  $scope.isAuthenticated = Principal.isAuthenticated;
                  $scope.isAdmin = _.contains(account.authorities, 'ROLE_ADMIN');
                });

                $scope.logout = function () {
                  Auth.logout();
                  $state.go('home');
                };
              });
