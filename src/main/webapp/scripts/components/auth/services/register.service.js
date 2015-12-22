(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('Register', Register);

  function Register($resource) {
    return $resource('api/register', {}, {});
  }

})();
