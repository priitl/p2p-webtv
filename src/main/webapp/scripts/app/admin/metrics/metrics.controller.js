(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('MetricsController', MetricsController);

  function MetricsController($scope, MonitoringService, $modal) {
    var vm = this;

    vm.metrics = {};
    vm.updatingMetrics = true;
    vm.refresh = refresh;
    vm.refreshThreadDumpData = refreshThreadDumpData;

    activate();

    ////////////////

    function activate() {
      refresh();
    }

    function refresh() {
      vm.updatingMetrics = true;
      MonitoringService.getMetrics().then(function (promise) {
        vm.metrics = promise;
        vm.updatingMetrics = false;
      }, function (promise) {
        vm.metrics = promise.data;
        vm.updatingMetrics = false;
      });
    }

    function refreshThreadDumpData() {
      MonitoringService.threadDump().then(function (data) {
        $modal.open({
          templateUrl: 'scripts/app/admin/metrics/metrics.modal.html',
          controller: 'MetricsModalController',
          controllerAs: 'vm',
          size: 'lg',
          resolve: {
            threadDump: function () {
              return data.content;
            }
          }
        });
      });
    }

    $scope.$watch('vm.metrics', function (newValue) {
      vm.servicesStats = {};
      vm.cachesStats = {};
      angular.forEach(newValue.timers, function (value, key) {
        if (key.indexOf('web.rest') !== -1 || key.indexOf('service') !== -1) {
          vm.servicesStats[key] = value;
        }
        if (key.indexOf('net.sf.ehcache.Cache') !== -1) {
          // remove gets or puts
          var index = key.lastIndexOf('.');
          var newKey = key.substr(0, index);

          // Keep the name of the domain
          index = newKey.lastIndexOf('.');
          vm.cachesStats[newKey] = {
            'name': newKey.substr(index + 1),
            'value': value
          };
        }
      });
    });
  }

})();

