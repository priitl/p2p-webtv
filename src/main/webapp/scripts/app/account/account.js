(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(accountConfig);

  function accountConfig($stateProvider) {
    $stateProvider
      .state('account', {
        abstract: true,
        parent: 'site'
      });
  }

})();
