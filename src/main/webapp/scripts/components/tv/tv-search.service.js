(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('TvSearch', TvSearch);

  function TvSearch($resource) {
    return $resource('api/tv/search/:title', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
