(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('notificationInterceptor', notificationInterceptor);

  function notificationInterceptor(AlertService) {
    return {
      response: response
    };

    ////////////////

    function response(response) {
      var alertKey = response.headers('X-wtvApp-alert');
      if (angular.isString(alertKey)) {
        AlertService.success(alertKey, {param: response.headers('X-wtvApp-params')});
      }
      return response;
    }
  }

})();
