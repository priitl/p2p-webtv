(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('User', User);

  function User($resource) {
    return $resource('api/users/:login', {}, {
      'query': {method: 'GET', isArray: true},
      'get': {
        method: 'GET',
        transformResponse: function (data) {
          data = angular.fromJson(data);
          return data;
        }
      },
      'save': {method: 'POST'},
      'update': {method: 'PUT'},
      'delete': {method: 'DELETE'}
    });
  }

})();
