(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvSearchController', TvSearchController);

  function TvSearchController(TvSearch, UserShow, Principal, ParseLinks, toastr, $translate, $stateParams) {
    var vm = this;

    vm.shows = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.page = 0;
    vm.setFavorite = setFavorite;

    activate();

    ////////////////

    function activate() {
      vm.searchTitle = $stateParams.title;
      loadAccount();
    }

    function loadAll() {
      TvSearch.query({page: vm.page, size: 20, title: vm.searchTitle}, function (result, headers) {
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
        toastr.success($translate.instant("tv.popular.messages.added"), show.name);
        UserShow.save(userShow);
      } else {
        toastr.warning($translate.instant("tv.popular.messages.removed"), show.name);
        UserShow.delete(userShow);
      }
    }
  }

})();

