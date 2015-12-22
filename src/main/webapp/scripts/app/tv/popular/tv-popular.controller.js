(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('TvPopularController', TvPopularController);

  function TvPopularController(TvPopular, UserShow, Principal, ParseLinks, toastr, $translate) {
    var vm = this;

    vm.shows = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.page = 0;
    vm.setFavorite = setFavorite;

    activate();

    ////////////////

    function activate() {
      loadAccount();
    }

    function loadAll() {
      TvPopular.query({page: vm.page, size: 20}, function (result, headers) {
        vm.links = ParseLinks.parse(headers('link'));
        for (var i = 0; i < result.length; i++) {
          vm.shows.push(result[i]);
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

    function setFavorite(show, isFavorite) {
      show.favorite = isFavorite;
      var userShow = {userLogin: vm.account.login, tmdbId: show.id, title: show.name};
      if (isFavorite) {
        toastr.success($translate.instant("tv.favorite.added"), show.name);
        UserShow.save(userShow);
      } else {
        toastr.warning($translate.instant("tv.favorite.removed"), show.name);
        UserShow.delete(userShow);
      }
    }
  }

})();

