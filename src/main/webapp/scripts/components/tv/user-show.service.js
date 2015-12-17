(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('UserShow', UserShow);

  function UserShow($resource) {
    return $resource('api/tv/user-show', {}, {
      'update': {method: 'PUT'},
      'delete': {method: 'DELETE'}
    });
  }

})();
