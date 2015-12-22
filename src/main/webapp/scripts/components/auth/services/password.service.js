(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('Password', Password)
    .factory('PasswordResetInit', PasswordResetInit)
    .factory('PasswordResetFinish', PasswordResetFinish);

  function Password($resource) {
    return $resource('api/account/change_password', {}, {});
  }

  function PasswordResetInit($resource) {
    return $resource('api/account/reset_password/init', {}, {})
  }

  function PasswordResetFinish($resource) {
    return $resource('api/account/reset_password/finish', {}, {})
  }

})();
