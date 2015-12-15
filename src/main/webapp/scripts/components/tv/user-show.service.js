(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('UserShow', UserShow);

  function UserShow($resource) {
    return $resource('api/user-show/', {}, {
      'query': {method: 'GET', isArray: true},
      'get': {
        method: 'GET',
        transformResponse: function (data) {
          data = angular.fromJson(data);
          return data;
        }
      },
      'update': {method: 'PUT'},
      'delete': {method: 'DELETE'}
    });
  }

})();
