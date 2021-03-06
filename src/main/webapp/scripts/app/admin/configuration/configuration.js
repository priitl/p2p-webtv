(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(configurationConfig);

  function configurationConfig($stateProvider) {
    $stateProvider
      .state('configuration', {
        parent: 'admin',
        url: '/configuration',
        data: {
          authorities: ['ROLE_ADMIN'],
          pageTitle: 'configuration.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/admin/configuration/configuration.html',
            controller: 'ConfigurationController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('configuration');
            return $translate.refresh();
          }]
        }
      });
  }

})();
