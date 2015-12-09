(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('Tv', Tv);

  function Tv($resource) {
    return $resource('api/tv/', {}, {
      'query': {method: 'GET', isArray: true},
      'get': {
        method: 'GET',
        transformResponse: function (data) {
          data = angular.fromJson(data);
          return data;
        }
      },
      'update': {method: 'PUT'}
    });
  }

})();
