(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('TvSearch', TvSearch);

  function TvSearch($resource) {
    return $resource('api/tv/search/:title', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
