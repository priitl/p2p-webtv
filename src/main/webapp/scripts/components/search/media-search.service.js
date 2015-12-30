(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('MediaSearch', MediaSearch);

  function MediaSearch($resource) {
    return $resource('api/media/search/:title', {}, {
      'query': {method: 'GET', isArray: true}
    });
  }

})();
