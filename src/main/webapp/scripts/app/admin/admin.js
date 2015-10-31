(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(adminConfig);

  function adminConfig($stateProvider) {
    $stateProvider
      .state('admin', {
        abstract: true,
        parent: 'site'
      });
  }

})();
