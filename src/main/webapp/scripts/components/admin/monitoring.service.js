(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('MonitoringService', MonitoringService);

  function MonitoringService($http) {
    return {
      getMetrics: getMetrics,
      checkHealth: checkHealth,
      threadDump: threadDump
    };

    ////////////////

    function getMetrics() {
      return $http.get('metrics/metrics').then(function (response) {
        return response.data;
      });
    }

    function checkHealth() {
      return $http.get('health').then(function (response) {
        return response.data;
      });
    }

    function threadDump() {
      return $http.get('dump').then(function (response) {
        return response.data;
      });
    }
  }

})();
