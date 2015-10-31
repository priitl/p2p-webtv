(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(loginConfig);

  function loginConfig($stateProvider) {
    $stateProvider
      .state('login', {
        parent: 'account',
        url: '/login',
        data: {
          authorities: [],
          pageTitle: 'login.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/account/login/login.html',
            controller: 'LoginController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('login');
            return $translate.refresh();
          }]
        }
      });
  }

})();
