(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('LogsService', LogsService);

  function LogsService($resource) {
    return $resource('api/logs', {}, {
      'findAll': {method: 'GET', isArray: true},
      'changeLevel': {method: 'PUT'}
    });
  }

})();
