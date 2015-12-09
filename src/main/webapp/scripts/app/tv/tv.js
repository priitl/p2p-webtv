(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(tvConfig);

  function tvConfig($stateProvider) {
    $stateProvider
      .state('tv', {
        parent: 'site',
        url: '/tv',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/tv/tv.html',
            controller: 'TvController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('tv');
            return $translate.refresh();
          }]
        }
      });
  }

})();
