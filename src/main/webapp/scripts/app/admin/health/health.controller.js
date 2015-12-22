(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('HealthController', HealthController);

  function HealthController(MonitoringService, $modal) {
    var vm = this;

    vm.addHealthObject = addHealthObject;
    vm.baseName = baseName;
    vm.flattenHealthData = flattenHealthData;
    vm.getLabelClass = getLabelClass;
    vm.getModuleName = getModuleName;
    vm.healthData = [];
    vm.hasSubSystem = hasSubSystem;
    vm.isHealthObject = isHealthObject;
    vm.refresh = refresh;
    vm.separator = '.';
    vm.subSystemName = subSystemName;
    vm.showHealth = showHealth;
    vm.transformHealthData = transformHealthData;
    vm.updatingHealth = true;

    activate();

    ////////////////

    function activate() {
      refresh();
    }

    function refresh() {
      vm.updatingHealth = true;
      MonitoringService.checkHealth().then(function (response) {
        vm.healthData = transformHealthData(response);
        vm.updatingHealth = false;
      }, function (response) {
        vm.healthData = transformHealthData(response.data);
        vm.updatingHealth = false;
      });
    }

    function getLabelClass(statusState) {
      if (statusState === 'UP') {
        return 'label-success';
      } else {
        return 'label-danger';
      }
    }

    function transformHealthData(data) {
      var response = [];
      flattenHealthData(response, null, data);
      return response;
    }

    function flattenHealthData(result, path, data) {
      angular.forEach(data, function (value, key) {
        if (isHealthObject(value)) {
          if (hasSubSystem(value)) {
            addHealthObject(result, false, value, getModuleName(path, key));
            flattenHealthData(result, getModuleName(path, key), value);
          } else {
            addHealthObject(result, true, value, getModuleName(path, key));
          }
        }
      });
      return result;
    }

    function getModuleName(path, name) {
      var result;
      if (path && name) {
        result = path + vm.separator + name;
      } else if (path) {
        result = path;
      } else if (name) {
        result = name;
      } else {
        result = '';
      }
      return result;
    }

    function showHealth(health) {
      $modal.open({
        templateUrl: 'scripts/app/admin/health/health.modal.html',
        controller: 'HealthModalController',
        controllerAs: 'vm',
        size: 'lg',
        resolve: {
          currentHealth: function () {
            return health;
          },
          baseName: function () {
            return baseName;
          },
          subSystemName: function () {
            return subSystemName;
          }

        }
      });
    }

    function addHealthObject(result, isLeaf, healthObject, name) {
      var healthData = {'name': name};
      var details = {};
      var hasDetails = false;

      angular.forEach(healthObject, function (value, key) {
        if (key === 'status' || key === 'error') {
          healthData[key] = value;
        } else {
          if (!isHealthObject(value)) {
            details[key] = value;
            hasDetails = true;
          }
        }
      });

      // Add the of the details
      if (hasDetails) {
        angular.extend(healthData, {'details': details});
      }

      // Only add nodes if they provide additional information
      if (isLeaf || hasDetails || healthData.error) {
        result.push(healthData);
      }
      return healthData;
    }

    function hasSubSystem(healthObject) {
      var result = false;
      angular.forEach(healthObject, function (value) {
        if (value && value.status) {
          result = true;
        }
      });
      return result;
    }

    function isHealthObject(healthObject) {
      var result = false;
      angular.forEach(healthObject, function (value, key) {
        if (key === 'status') {
          result = true;
        }
      });
      return result;
    }

    function baseName(name) {
      if (name) {
        var split = name.split('.');
        return split[0];
      }
    }

    function subSystemName(name) {
      if (name) {
        var split = name.split('.');
        split.splice(0, 1);
        var remainder = split.join('.');
        return remainder ? ' - ' + remainder : '';
      }
    }

  }

})();

