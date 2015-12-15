(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvController', TvController);

  function TvController(Tv, UserShow, Principal, ParseLinks) {
    var vm = this;

    vm.popularTv = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.reset = reset;
    vm.page = 1;
    vm.setFavorite = setFavorite;

    activate();

    ////////////////

    function activate() {
      loadAll();
      loadAccount();
    }

    function loadAll() {
      Tv.query({page: vm.page, size: 20}, function (result, headers) {
        vm.links = ParseLinks.parse(headers('link'));
        for (var i = 0; i < result.length; i++) {
          vm.popularTv.push(result[i]);
        }
      });
    }

    function loadPage(page) {
      vm.page = page;
      vm.loadAll();
    }

    function loadAccount() {
      Principal.identity().then(function (account) {
        vm.account = account;
      });
    }

    function reset() {
      vm.page = 0;
      vm.popularTv = [];
      vm.loadAll();
    }

    function setFavorite(show, isFavorite) {
      show.favorite = isFavorite;

      var userShow = {userLogin: vm.account.login, showName: show.name};
      if (isFavorite) {
        UserShow.save(userShow);
      } else {
        UserShow.delete(userShow);
      }
    }
  }

})();

