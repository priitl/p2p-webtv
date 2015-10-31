'use strict';

angular.module('maurusApp')
  .factory('Register', function ($resource) {
             return $resource('api/register', {}, {});
           });


