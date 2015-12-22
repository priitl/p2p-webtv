(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('ConfigurationController', ConfigurationController);

  function ConfigurationController(ConfigurationService) {
    var vm = this;

    vm.configuration = [];

    activate();

    ////////////////

    function activate() {
      ConfigurationService.get().then(function (configuration) {
        vm.configuration = configuration;
      });
    }
  }

})();


