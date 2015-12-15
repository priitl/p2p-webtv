(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvController', TvController);

  function TvController(Tv, ParseLinks) {
    var vm = this;

    vm.popularTv = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.reset = reset;
    vm.page = 1;

    activate();

    ////////////////

    function activate() {
      loadAll();
    }

    function loadAll() {
      Tv.query({page: vm.page, size: 20}, function(result, headers) {
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

    function reset() {
      vm.page = 0;
      vm.popularTv = [];
      vm.loadAll();
    }
  }

})();

