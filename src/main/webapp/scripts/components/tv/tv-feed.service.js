(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('TvFeed', TvFeed);

  function TvFeed($resource) {
    return $resource('api/tv/feed', {}, {
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
