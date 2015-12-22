(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('UserManagementDialogController', UserManagementDialogController);

  function UserManagementDialogController($uibModalInstance, entity, User, Language) {
    var vm = this;
    vm.user = entity;
    vm.authorities = ["ROLE_USER", "ROLE_ADMIN"];
    vm.clear = clear;
    vm.save = save;

    activate();

    ////////////////

    function activate() {
      Language.getAll().then(function (languages) {
        vm.languages = languages;
      });
    }

    function save() {
      vm.isSaving = true;
      if (vm.user.id != null) {
        User.update(vm.user, onSaveSuccess, onSaveError);
      } else {
        User.save(vm.user, onSaveSuccess, onSaveError);
      }
    }

    function clear() {
      $uibModalInstance.dismiss('cancel');
    }

    function onSaveSuccess(result) {
      vm.isSaving = false;
      $uibModalInstance.close(result);
    }

    function onSaveError() {
      vm.isSaving = false;
    }

  }

})();

