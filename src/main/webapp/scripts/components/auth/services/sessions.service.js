(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('Sessions', Sessions);

  function Sessions($resource) {
    return $resource('api/account/sessions/:series', {}, {
      'getAll': {method: 'GET', isArray: true}
    });
  }

})();
