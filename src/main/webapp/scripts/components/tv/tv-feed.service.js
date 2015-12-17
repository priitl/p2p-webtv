(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('TvFeed', TvFeed);

  function TvFeed($resource) {
    return $resource('api/tv/feed', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
