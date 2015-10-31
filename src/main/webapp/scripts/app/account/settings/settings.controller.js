(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('SettingsController', SettingsController);

  function SettingsController(Principal, Auth, Language, $translate) {
    var vm = this;

    vm.save = save;
    vm.settingsAccount = {};

    activate();

    ////////////////

    function activate() {
      vm.success = null;
      vm.error = null;
      Principal.identity(true).then(function (account) {
        vm.settingsAccount = account;
      });
    }

    function save() {
      Auth.updateAccount(vm.settingsAccount).then(function () {
        vm.error = null;
        vm.success = 'OK';
        Principal.identity().then(function (account) {
          vm.settingsAccount = account;
        });
        Language.getCurrent().then(function (current) {
          if (vm.settingsAccount.langKey !== current) {
            $translate.use(vm.settingsAccount.langKey);
          }
        });
      }).catch(function () {
        vm.success = null;
        vm.error = 'ERROR';
      });
    }
  }

})();

