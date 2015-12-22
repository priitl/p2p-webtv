(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('AuditsController', AuditsController);

  function AuditsController($filter, AuditsService) {
    var vm = this;

    vm.today = today;
    vm.previousMonth = previousMonth;
    vm.onChangeDate = onChangeDate;

    activate();

    ////////////////

    function activate() {
      today();
      previousMonth();
      onChangeDate();
    }

    function onChangeDate() {
      var dateFormat = 'yyyy-MM-dd';
      var fromDate = $filter('date')(vm.fromDate, dateFormat);
      var toDate = $filter('date')(vm.toDate, dateFormat);

      AuditsService.findByDates(fromDate, toDate).then(function (data) {
        vm.audits = data;
      });
    }

    // Date picker configuration
    function today() {
      // Today + 1 day - needed if the current day must be included
      var today = new Date();
      vm.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
    }

    function previousMonth() {
      var fromDate = new Date();
      if (fromDate.getMonth() === 0) {
        fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
      } else {
        fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
      }

      vm.fromDate = fromDate;
    }

  }

})();
