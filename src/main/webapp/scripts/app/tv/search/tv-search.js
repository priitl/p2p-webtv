(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(tvSearchConfig);

  function tvSearchConfig($stateProvider) {
    $stateProvider
      .state('tv-search', {
        parent: 'site',
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
