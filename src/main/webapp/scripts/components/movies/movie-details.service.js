(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('MovieDetails', MovieDetails);

  function MovieDetails($resource) {
    return $resource('api/movies/:tmdbId', {}, {
      'get': {
        method: 'GET',
        transformResponse: function (data) {
          data = angular.fromJson(data);
          return data;
        }
      }
    });
  }

})();
