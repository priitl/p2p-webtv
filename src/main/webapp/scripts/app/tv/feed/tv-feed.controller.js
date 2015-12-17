(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvFeedController', TvFeedController);

  function TvFeedController(TvFeed) {
    var vm = this;
    var allItems = [];
    var cachedItems = [];
    var loaded = false;
    var pageSize = 20;

    vm.feedItems = [];
    vm.filterActive = filterActive;
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

    function filterActive() {
      if (vm.filter.showTitle.length > 2 && !cachedItems.length) {
        cachedItems = vm.feedItems.slice(0);
        vm.feedItems = _.where(allItems, function (item) {
          return item.showTitle.indexOf(vm.filter.showTitle) >= 0;
        });
      } else if (!vm.filter.showTitle.length && cachedItems.length) {
        vm.feedItems = cachedItems.slice(0);
        cachedItems = [];
      }
    }

    function addFeedItems() {
      for (var i = (vm.page - 1) * pageSize; i < allItems.length && i < (vm.page) * pageSize; i++) {
        vm.feedItems.push(allItems[i]);
      }
    }

  }

})();

