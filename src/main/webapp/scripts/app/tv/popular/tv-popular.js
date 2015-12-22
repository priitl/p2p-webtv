(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(tvPopularConfig);

  function tvPopularConfig($stateProvider) {
    $stateProvider
      .state('tv-popular', {
        parent: 'tv',
        url: '/tv/popular',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.tv.popular'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/tv/popular/tv-popular.html',
            controller: 'TvPopularController',
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
