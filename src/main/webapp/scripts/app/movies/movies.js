(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(moviesConfig);

  function moviesConfig($stateProvider) {
    $stateProvider
      .state('movies', {
        abstract: true,
        parent: 'site'
      });
  }

})();
