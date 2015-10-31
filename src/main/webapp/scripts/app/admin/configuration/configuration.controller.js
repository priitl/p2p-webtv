(function () {
  'use strict';

  angular
    .module('maurusApp')
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


