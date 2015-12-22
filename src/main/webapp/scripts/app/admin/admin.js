(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(adminConfig);

  function adminConfig($stateProvider) {
    $stateProvider
      .state('admin', {
        abstract: true,
        parent: 'site'
      });
  }

})();
