(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('UserShow', UserShow);

  function UserShow($resource) {
    return $resource('api/tv/user-show', {}, {
      'update': {method: 'PUT'},
      'delete': {method: 'DELETE'}
    });
  }

})();
