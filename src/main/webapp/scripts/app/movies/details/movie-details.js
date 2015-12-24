(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(movieDetailsConfig);

  function movieDetailsConfig($stateProvider) {
    $stateProvider
      .state('movie-details', {
        parent: 'site',
        url: '/movies/:tmdbId',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.movies.details'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/movies/details/movie-details.html',
            controller: 'MovieDetailsController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('movies');
            return $translate.refresh();
          }]
        }
      });
  }

})();
