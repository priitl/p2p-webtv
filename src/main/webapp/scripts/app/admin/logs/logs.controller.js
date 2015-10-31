(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('LogsController', LogsController);

  function LogsController(LogsService) {
    var vm = this;

    vm.changeLevel = changeLevel;
    vm.loggers = [];

    activate();

    ////////////////

    function activate() {
      vm.loggers = LogsService.findAll();
    }

    function changeLevel(name, level) {
      LogsService.changeLevel({name: name, level: level}, function () {
        vm.loggers = LogsService.findAll();
      });
    }
  }

})();

