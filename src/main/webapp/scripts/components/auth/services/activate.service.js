(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('Activate', Activate);

  function Activate($resource) {
    return $resource('api/activate', {},
      {'get': {method: 'GET', params: {}, isArray: false}}
    );
  }

})();
