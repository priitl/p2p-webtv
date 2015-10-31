(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(entityConfig);

  function entityConfig($stateProvider) {
    $stateProvider
      .state('entity', {
        abstract: true,
        parent: 'site'
      });
  }

})();
