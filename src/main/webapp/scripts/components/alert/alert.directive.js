(function () {
  'use strict';

  angular
    .module('maurusApp')
    .directive('jhAlert', jhAlert)
    .directive('jhAlertError', jhAlertError);

  function jhAlert() {
    return {
      bindToController: true,
      controller: JhAlertController,
      controllerAs: 'vm',
      restrict: 'E',
      templateUrl: 'scripts/components/alert/alert.directive.html'
    };
  }

  function JhAlertController($scope, AlertService) {
    var vm = this;
    vm.alerts = AlertService.get();
    $scope.$on('$destroy', function () {
      vm.alerts = [];
    });
  }

  function jhAlertError() {
    return {
      bindToController: true,
      controller: JhAlertErrorController,
      controllerAs: 'vm',
      restrict: 'E',
      templateUrl: 'scripts/components/alert/alert.directive.html'
    };
  }

  function JhAlertErrorController($scope, $rootScope, $translate, AlertService) {
    var vm = this;
    vm.alerts = AlertService.get();

    var cleanHttpErrorListener = $rootScope.$on('maurusApp.httpError', function (event, httpResponse) {
      event.stopPropagation();
      switch (httpResponse.status) {
        case 0:
          addErrorAlert("Server not reachable", 'error.serverNotReachable');
          break;
        case 400:
          if (httpResponse.data && httpResponse.data.fieldErrors) {
            for (var i = 0; i < httpResponse.data.fieldErrors.length; i++) {
              var fieldError = httpResponse.data.fieldErrors[i];
              var convertedField = fieldError.field.replace(/\[\d*\]/g, "[]");
              var fieldName = $translate.instant('maurusApp.' + fieldError.objectName + '.' + convertedField);
              addErrorAlert('Field ' + fieldName + ' cannot be empty', 'error.' + fieldError.message,
                {fieldName: fieldName});
            }
          } else if (httpResponse.data && httpResponse.data.message) {
            addErrorAlert(httpResponse.data.message, httpResponse.data.message, httpResponse.data);
          } else {
            addErrorAlert(httpResponse.data);
          }
          break;
        default:
          if (httpResponse.data && httpResponse.data.message) {
            addErrorAlert(httpResponse.data.message);
          } else {
            addErrorAlert(JSON.stringify(httpResponse));
          }
      }
    });

    $scope.$on('$destroy', function () {
      if (cleanHttpErrorListener !== undefined && cleanHttpErrorListener !== null) {
        cleanHttpErrorListener();
      }
    });

    var addErrorAlert = function (message, key, data) {
      key = key && key != null ? key : message;
      AlertService.error(key, data);
    }
  }

})();
