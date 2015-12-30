(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(tvSearchConfig);

  function tvSearchConfig($stateProvider) {
    $stateProvider
      .state('media-search', {
        parent: 'site',
        url: '/search/:title',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.search.main'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/search/media-search.html',
            controller: 'MediaSearchController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', function ($translate) {
            return $translate.refresh();
          }]
        }
      });
  }

})();
