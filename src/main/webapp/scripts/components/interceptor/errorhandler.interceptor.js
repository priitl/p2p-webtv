(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('errorHandlerInterceptor', errorHandlerInterceptor);

  function errorHandlerInterceptor($q, $rootScope) {
    return {
      'responseError': responseError
    };

    ////////////////

    function responseError(response) {
      if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )) {
        $rootScope.$emit('wtvApp.httpError', response);
      }
      return $q.reject(response);
    }
  }

})();
