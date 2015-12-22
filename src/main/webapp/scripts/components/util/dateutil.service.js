(function () {
  'use strict';

  angular
    .module('wtvApp')
    .service('DateUtils', DateUtils);

  function DateUtils($filter) {
    this.convertLocaleDateToServer = convertLocaleDateToServer;
    this.convertLocaleDateFromServer = convertLocaleDateFromServer;
    this.convertDateTimeFromServer = convertDateTimeFromServer;

    ////////////////

    function convertLocaleDateToServer(date) {
      if (date) {
        return $filter('date')(date, 'yyyy-MM-dd');
      } else {
        return null;
      }
    }

    function convertLocaleDateFromServer(date) {
      if (date) {
        var dateString = date.split("-");
        return new Date(dateString[0], dateString[1] - 1, dateString[2]);
      }
      return null;
    }

    function convertDateTimeFromServer(date) {
      if (date) {
        return new Date(date);
      } else {
        return null;
      }
    }
  }

})();
