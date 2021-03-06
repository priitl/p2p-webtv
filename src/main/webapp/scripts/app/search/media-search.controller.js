(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('MediaSearchController', MediaSearchController);

  function MediaSearchController(MediaSearch, UserShow, Principal, ParseLinks, toastr, $translate, $stateParams) {
    var vm = this;

    vm.results = [];
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
      MediaSearch.query({page: vm.page, size: 20, title: vm.searchTitle}, function (result, headers) {
        vm.links = ParseLinks.parse(headers('link'));
        for (var i = 0; i < result.length; i++) {
          vm.results.push(result[i]);
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
      var userShow = {userLogin: vm.account.login, tmdbId: show.tmdbId, title: show.title};
      if (isFavorite) {
        toastr.success($translate.instant("tv.favorite.added"), show.title);
        UserShow.save(userShow);
      } else {
        toastr.warning($translate.instant("tv.favorite.removed"), show.title);
        UserShow.delete(userShow);
      }
    }
  }

})();

