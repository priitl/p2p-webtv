(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(moviePopularConfig);

  function moviePopularConfig($stateProvider) {
    $stateProvider
      .state('movies-popular', {
        parent: 'movies',
        url: '/movies/popular',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.movies.popular'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/movies/popular/movies-popular.html',
            controller: 'MoviesPopularController',
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
