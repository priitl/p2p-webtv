(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(tvFeedConfig);

  function tvFeedConfig($stateProvider) {
    $stateProvider
      .state('tv-feed', {
        parent: 'tv',
        url: '/tv/feed',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.tv.feed'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/tv/feed/tv-feed.html',
            controller: 'TvFeedController',
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
