(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(accountConfig);

  function accountConfig($stateProvider) {
    $stateProvider
      .state('account', {
        abstract: true,
        parent: 'site'
      });
  }

})();
