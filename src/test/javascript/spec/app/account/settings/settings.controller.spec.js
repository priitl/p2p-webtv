'use strict';

describe('Controllers Tests ', function () {

  beforeEach(mockApiAccountCall);
  beforeEach(mockI18nCalls);

  describe('SettingsController', function () {

    var $scope, $q; // actual implementations
    var MockPrincipal, MockAuth; // mocks
    var createController; // local utility functions

    beforeEach(inject(function ($injector) {
      $q = $injector.get('$q');
      $scope = $injector.get("$rootScope").$new();
      MockAuth = jasmine.createSpyObj('MockAuth', ['updateAccount']);
      MockPrincipal = jasmine.createSpyObj('MockPrincipal', ['identity']);
      var locals = {
        '$scope': $scope,
        'Principal': MockPrincipal,
        'Auth': MockAuth
      };
      createController = function () {
        $injector.get('$controller')('SettingsController as vm', locals);
      }
    }));

    it('should send the current identity upon save', function () {
      //GIVEN
      var accountValues = {
        firstName: "John",
        lastName: "Doe"
      };
      MockPrincipal.identity.and.returnValue($q.resolve(accountValues));
      MockAuth.updateAccount.and.returnValue($q.resolve());
      $scope.$apply(createController);

      //WHEN
      $scope.vm.save();

      //THEN
      expect(MockPrincipal.identity).toHaveBeenCalled();
      expect(MockAuth.updateAccount).toHaveBeenCalledWith(accountValues);
      expect($scope.vm.settingsAccount).toEqual(accountValues);
    });
  });
});
