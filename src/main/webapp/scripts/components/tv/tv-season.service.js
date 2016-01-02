(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('TvSeason', TvSeason);

  function TvSeason($resource) {
    return $resource('api/tv/:tmdbId/season/:seasonNumber', {}, {
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
