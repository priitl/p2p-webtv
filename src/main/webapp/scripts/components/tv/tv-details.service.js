(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('TvDetails', TvDetails);

  function TvDetails($resource) {
    return $resource('api/tv/:tmdbId', {}, {
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
