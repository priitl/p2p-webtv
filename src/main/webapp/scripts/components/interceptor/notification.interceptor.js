'use strict';

angular.module('maurusApp')
  .factory('notificationInterceptor', function ($q, AlertService) {
             return {
               response: function (response) {
                 var alertKey = response.headers('X-maurusApp-alert');
                 if (angular.isString(alertKey)) {
                   AlertService.success(alertKey, {param: response.headers('X-maurusApp-params')});
                 }
                 return response;
               }
             };
           });
