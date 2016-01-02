(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(movieDetailsConfig);

  function movieDetailsConfig($stateProvider) {
    $stateProvider
      .state('tv-details', {
        parent: 'site',
        url: '/tv/:tmdbId',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.tv.details'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/tv/details/tv-details.html',
            controller: 'TvDetailsController',
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
