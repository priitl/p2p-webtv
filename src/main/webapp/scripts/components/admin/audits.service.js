(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('AuditsService', AuditsService);

  function AuditsService($http) {
    return {
      findAll: findAll,
      findByDates: findByDates
    };

    ////////////////

    function findAll() {
      return $http.get('api/audits/').then(function (response) {
        return response.data;
      });
    }

    function findByDates(fromDate, toDate) {
      var formatDate = function (dateToFormat) {
        if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
          return dateToFormat.getYear() + '-' + dateToFormat.getMonth() + '-' + dateToFormat.getDay();
        }
        return dateToFormat;
      };

      var parameters = {params: {fromDate: formatDate(fromDate), toDate: formatDate(toDate)}};
      return $http.get('api/audits/', parameters).then(function (response) {
        return response.data;
      });
    }
  }

})();
