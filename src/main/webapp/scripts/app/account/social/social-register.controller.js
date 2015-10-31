(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('SocialRegisterController', SocialRegisterController);

  function SocialRegisterController($filter, $stateParams) {
    var vm = this;

    activate();

    ////////////////

    function activate() {
      vm.provider = $stateParams.provider;
      vm.providerLabel = $filter('capitalize')(vm.provider);
      vm.success = $stateParams.success;
      vm.error = !vm.success;
    }
  }

})();

