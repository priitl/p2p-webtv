(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(healthConfig);

  function healthConfig($stateProvider) {
    $stateProvider
      .state('health', {
        parent: 'admin',
        url: '/health',
        data: {
          authorities: ['ROLE_ADMIN'],
          pageTitle: 'health.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/admin/health/health.html',
            controller: 'HealthController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('health');
            return $translate.refresh();
          }]
        }
      });
  }

})();
