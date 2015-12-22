(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(resetRequestConfig);

  function resetRequestConfig($stateProvider) {
    $stateProvider
      .state('requestReset', {
        parent: 'account',
        url: '/reset/request',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/account/reset/request/reset.request.html',
            controller: 'ResetRequestController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('reset');
            return $translate.refresh();
          }]
        }
      });
  }

})();
