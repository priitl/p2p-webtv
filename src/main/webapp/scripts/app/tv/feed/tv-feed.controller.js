(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvFeedController', TvFeedController);

  function TvFeedController(TvFeed, ParseLinks) {
    var vm = this;

    vm.feedItems = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.page = 0;

    ////////////////

    function loadAll() {
      TvFeed.query({page: vm.page, size: 20}, function (result, headers) {
        vm.links = ParseLinks.parse(headers('link'));
        for (var i = 0; i < result.length; i++) {
          vm.feedItems.push(result[i]);
        }
      });
    }

    function loadPage(page) {
      vm.page = page;
      vm.loadAll();
    }

  }

})();

