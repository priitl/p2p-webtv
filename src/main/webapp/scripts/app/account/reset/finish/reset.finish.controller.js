(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('ResetFinishController', ResetFinishController);

  function ResetFinishController($stateParams, $timeout, Auth) {
    var vm = this;

    vm.finishReset = finishReset;
    vm.resetAccount = {};
    activate();

    ////////////////

    function activate() {
      vm.keyMissing = $stateParams.key === undefined;
      vm.doNotMatch = null;
      $timeout(function () {
        angular.element('[ng-model="resetAccount.password"]').focus();
      });
    }

    function finishReset() {
      if (vm.resetAccount.password !== vm.confirmPassword) {
        vm.doNotMatch = 'ERROR';
      } else {
        Auth.resetPasswordFinish({
          key: $stateParams.key,
          newPassword: vm.resetAccount.password
        }).then(function () {
          vm.success = 'OK';
        }).catch(function () {
          vm.success = null;
          vm.error = 'ERROR';
        });
      }
    }
  }

})();


