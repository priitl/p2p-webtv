(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('UserManagementDeleteController', UserManagementDeleteController);

  function UserManagementDeleteController($uibModalInstance, entity, User) {
    var vm = this;
    vm.user = entity;
    vm.clear = clear;
    vm.confirmDelete = confirmDelete;

    ////////////////

    function clear() {
      $uibModalInstance.dismiss('cancel');
    }

    function confirmDelete(login) {
      User.delete({login: login},
        function () {
          $uibModalInstance.close(true);
        });
    }
  }

})();

