'use strict';

angular.module('maurusApp')
  .controller('HeaderController', function ($scope, $state, Auth, Principal) {
                $scope.isAuthenticated = Principal.isAuthenticated;

                $scope.logout = function () {
                  Auth.logout();
                  $state.go('home');
                };
              });
