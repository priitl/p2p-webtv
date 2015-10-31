(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('ActivationController', ActivationController);

  function ActivationController($stateParams, Auth) {
    var vm = this;

    ////////////////

    Auth.activateAccount({key: $stateParams.key}).then(function () {
      vm.error = null;
      vm.success = 'OK';
    }).catch(function () {
      vm.success = null;
      vm.error = 'ERROR';
    });
  }

})();

