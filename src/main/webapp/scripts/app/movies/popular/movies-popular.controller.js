(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('MoviesPopularController', MoviesPopularController);

  function MoviesPopularController(MoviePopular, ParseLinks) {
    var vm = this;

    vm.movies = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.page = 0;

    ////////////////

    function loadAll() {
      MoviePopular.query({page: vm.page, size: 20}, function (result, headers) {
        vm.links = ParseLinks.parse(headers('link'));
        for (var i = 0; i < result.length; i++) {
          vm.movies.push(result[i]);
        }
      });
    }

    function loadPage(page) {
      vm.page = page;
      vm.loadAll();
    }
  }

})();

