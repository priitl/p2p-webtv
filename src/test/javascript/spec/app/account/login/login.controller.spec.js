'use strict';

describe('Controllers Tests ', function () {

  beforeEach(module('wtvApp'));

  describe('LoginController', function () {
    var $scope;

    beforeEach(inject(function ($rootScope, $controller) {
      $scope = $rootScope.$new();
      $controller('LoginController as vm', {$scope: $scope});
    }));

    it('should set remember Me', function () {
      expect($scope.vm.rememberMe).toBeTruthy();
    });
  });
});
