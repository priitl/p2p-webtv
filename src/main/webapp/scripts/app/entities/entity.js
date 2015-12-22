(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(entityConfig);

  function entityConfig($stateProvider) {
    $stateProvider
      .state('entity', {
        abstract: true,
        parent: 'site'
      });
  }

})();
