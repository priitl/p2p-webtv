(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(tvPopularConfig);

  function tvPopularConfig($stateProvider) {
    $stateProvider
      .state('tv-search', {
        parent: 'tv',
        url: '/tv/search/:title',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.tv.search'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/tv/search/tv-search.html',
            controller: 'TvSearchController',
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
