(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('Register', Register);

  function Register($resource) {
    return $resource('api/register', {}, {});
  }

})();
