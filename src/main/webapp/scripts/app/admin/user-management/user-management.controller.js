(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('UserManagementController', UserManagementController);

  function UserManagementController(User, ParseLinks, Language) {
    var vm = this;

    vm.authorities = ["ROLE_USER", "ROLE_ADMIN"];
    vm.clear = clear;
    vm.editForm = {};
    vm.loadAll = loadUsers;
    vm.loadPage = loadPage;
    vm.page = 0;
    vm.refresh = refresh;
    vm.save = save;
    vm.setActive = setActive;
    vm.showUpdate = showUpdate;
    vm.users = [];

    activate();

    ////////////////

    function activate() {
      loadUsers();
      loadLanguages();
    }

    function loadUsers() {
      User.query({page: vm.page, per_page: 20}, function (result, headers) {
        vm.links = ParseLinks.parse(headers('link'));
        vm.users = result;
      });
    }

    function loadLanguages() {
      Language.getAll().then(function (languages) {
        vm.languages = languages;
      });
    }

    function loadPage(page) {
      vm.page = page;
      loadUsers();
    }

    function setActive(user, isActivated) {
      user.activated = isActivated;
      User.update(user, function () {
        loadUsers();
        clear();
      });
    }

    function showUpdate(login) {
      User.get({login: login}, function (result) {
        vm.user = result;
        $('#saveUserModal').modal('show');
      });
    }

    function save() {
      User.update(vm.user, refresh);
    }

    function refresh() {
      loadUsers();
      $('#saveUserModal').modal('hide');
      clear();
    }

    function clear() {
      vm.user = {
        id: null, login: null, firstName: null, lastName: null, email: null,
        activated: null, langKey: null, createdBy: null, createdDate: null,
        lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
        resetKey: null, authorities: null
      };
      vm.editForm.$setPristine();
      vm.editForm.$setUntouched();
    }
  }

})();
