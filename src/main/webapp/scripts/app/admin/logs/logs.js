(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(logsConfig);

  function logsConfig($stateProvider) {
    $stateProvider
      .state('logs', {
        parent: 'admin',
        url: '/logs',
        data: {
          authorities: ['ROLE_ADMIN'],
          pageTitle: 'logs.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/admin/logs/logs.html',
            controller: 'LogsController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('logs');
            return $translate.refresh();
          }]
        }
      });
  }

})();
