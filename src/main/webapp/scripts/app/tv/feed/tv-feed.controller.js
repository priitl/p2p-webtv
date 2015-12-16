(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvFeedController', TvFeedController);

  function TvFeedController(TvFeed) {
    var vm = this;
    var allItems = [];
    var loaded = false;
    var pageSize = 20;

    vm.feedItems = [];
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.page = 0;

    ////////////////

    function loadAll() {
      TvFeed.query(function (result) {
        allItems = result;
        addFeedItems();
      });
      loaded = true;
    }

    function loadPage(page) {
      vm.page = page;
      if (!loaded) {
        loadAll();
      } else {
        addFeedItems();
      }
    }

    function addFeedItems() {
      for (var i = (vm.page - 1) * pageSize; i < allItems.length && i < (vm.page) * pageSize; i++) {
        vm.feedItems.push(allItems[i]);
      }
    }

  }

})();

