(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('notificationInterceptor', notificationInterceptor);

  function notificationInterceptor(AlertService) {
    return {
      response: response
    };

    ////////////////

    function response(response) {
      var alertKey = response.headers('X-maurusApp-alert');
      if (angular.isString(alertKey)) {
        AlertService.success(alertKey, {param: response.headers('X-maurusApp-params')});
      }
      return response;
    }
  }

})();
