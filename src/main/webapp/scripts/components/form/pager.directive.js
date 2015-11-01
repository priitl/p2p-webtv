/* globals $ */
(function () {
  'use strict';

  angular
    .module('maurusApp')
    .directive('maurusAppPager', maurusAppPager);

  function maurusAppPager() {
    return {
      templateUrl: 'scripts/components/form/pager.html'
    };
  }

})();
