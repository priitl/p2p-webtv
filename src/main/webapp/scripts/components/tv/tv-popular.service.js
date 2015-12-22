(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('TvPopular', TvPopular);

  function TvPopular($resource) {
    return $resource('api/tv/popular', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
