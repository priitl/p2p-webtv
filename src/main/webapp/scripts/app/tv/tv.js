(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(accountConfig);

  function accountConfig($stateProvider) {
    $stateProvider
      .state('tv', {
        abstract: true,
        parent: 'site'
      });
  }

})();
