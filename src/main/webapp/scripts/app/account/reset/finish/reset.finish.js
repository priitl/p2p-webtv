(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(resetFinishConfig);

  function resetFinishConfig($stateProvider) {
    $stateProvider
      .state('finishReset', {
        parent: 'account',
        url: '/reset/finish?key',
        data: {
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/account/reset/finish/reset.finish.html',
            controller: 'ResetFinishController',
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
