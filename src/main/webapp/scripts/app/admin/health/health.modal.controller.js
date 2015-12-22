'use strict';

angular.module('wtvApp')
  .controller('HealthModalController', function ($scope, $modalInstance, currentHealth, baseName, subSystemName) {

              });

(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('HealthModalController', HealthModalController);

  function HealthModalController($modalInstance, currentHealth, baseName, subSystemName) {
    var vm = this;

    vm.cancel = cancel;

    activate();

    ////////////////

    function activate() {
      vm.currentHealth = currentHealth;
      vm.baseName = baseName;
      vm.subSystemName = subSystemName;
    }

    function cancel() {
      $modalInstance.dismiss('cancel');
    }
  }

})();

