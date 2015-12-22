(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('TvFeed', TvFeed);

  function TvFeed($resource) {
    return $resource('api/tv/feed', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
