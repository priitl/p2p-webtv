(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('SettingsController', SettingsController);

  function SettingsController(Principal, Auth, Language, $translate, $scope, $cookies, toastr) {
    var vm = this;

    vm.byteSize = byteSize;
    vm.save = save;
    vm.setPicture = setPicture;
    vm.settingsAccount = {};

    activate();

    ////////////////

    function activate() {
      Principal.identity(true).then(function (account) {
        vm.settingsAccount = account;
        vm.settingsAccount.apiUrl = $cookies.get("apiUrl");
      });
    }

    function save() {
      Auth.updateAccount(vm.settingsAccount).then(function (response) {
        Principal.identity().then(function (account) {
          vm.settingsAccount = account;
        });
        Language.getCurrent().then(function (current) {
          if (vm.settingsAccount.langKey !== current) {
            $translate.use(vm.settingsAccount.langKey);
          }
        });
        if(angular.isDefined(vm.settingsAccount.apiUrl) && vm.settingsAccount.apiUrl.length) {
          $cookies.put("apiUrl", vm.settingsAccount.apiUrl);
        } else {
          $cookies.remove("apiUrl");
        }
        toastr.success($translate.instant('settings.messages.success'));
      }).catch(function () {
      });
    }

    function byteSize(base64String) {
      if (!angular.isString(base64String)) {
        return '';
      }
      function endsWith(suffix, str) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
      }

      function paddingSize(base64String) {
        if (endsWith('==', base64String)) {
          return 2;
        }
        if (endsWith('=', base64String)) {
          return 1;
        }
        return 0;
      }

      function size(base64String) {
        return base64String.length / 4 * 3 - paddingSize(base64String);
      }

      function formatAsBytes(size) {
        return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
      }

      return formatAsBytes(size(base64String));
    }

    function setPicture($file, settingsAccount) {
      if ($file && $file.$error == 'pattern') {
        return;
      }
      if ($file) {
        var fileReader = new FileReader();
        fileReader.readAsDataURL($file);
        fileReader.onload = function (e) {
          var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
          $scope.$apply(function () {
            settingsAccount.picture = base64Data;
            settingsAccount.pictureContentType = $file.type;
          });
        };
      }
    }
  }

})();

