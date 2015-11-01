(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('ConfigurationService', ConfigurationService);

  function ConfigurationService($filter, $http) {
    return {
      get: get
    };

    ////////////////

    function get() {
      return $http.get('configprops').then(function (response) {
        var properties = [];
        angular.forEach(response.data, function (data) {
          properties.push(data);
        });
        var orderBy = $filter('orderBy');
        return orderBy(properties, 'prefix');
      });
    }
  }

})();
