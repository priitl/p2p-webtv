/* globals $ */
(function () {
  'use strict';

  angular
    .module('maurusApp')
    .directive('maurusAppPagination', maurusAppPagination);

  function maurusAppPagination() {
    return {
      templateUrl: 'scripts/components/form/pagination.html'
    };
  }

})();
