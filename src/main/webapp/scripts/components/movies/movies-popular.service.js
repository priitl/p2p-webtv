(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('MoviePopular', MoviePopular);

  function MoviePopular($resource) {
    return $resource('api/movies/popular', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
