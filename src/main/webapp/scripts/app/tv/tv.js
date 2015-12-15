(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(accountConfig);

  function accountConfig($stateProvider) {
    $stateProvider
      .state('tv', {
        abstract: true,
        parent: 'site'
      });
  }

})();
