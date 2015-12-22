(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(mainConfig);

  function mainConfig($stateProvider) {
    $stateProvider
      .state('home', {
        parent: 'site',
        url: '/',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/main/main.html',
            controller: 'MainController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('main');
            return $translate.refresh();
          }]
        }
      });
  }

})();
