(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('Activate', Activate);

  function Activate($resource) {
    return $resource('api/activate', {},
      {'get': {method: 'GET', params: {}, isArray: false}}
    );
  }

})();
