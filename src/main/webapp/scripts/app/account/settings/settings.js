(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(settingsConfig);

  function settingsConfig($stateProvider) {
    $stateProvider
      .state('settings', {
        parent: 'account',
        url: '/settings',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.account.settings'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/account/settings/settings.html',
            controller: 'SettingsController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('settings');
            return $translate.refresh();
          }]
        }
      });
  }

})();
