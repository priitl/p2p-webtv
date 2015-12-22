(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('TvFeedController', TvFeedController);

  function TvFeedController(TvFeed, $cookies) {
    var vm = this;
    var allItems = [];
    var cachedItems = [];
    var loaded = false;
    var pageSize = 20;

    vm.feedItems = [];
    vm.clearFilter = clearFilter;
    vm.filterActive = filterActive;
    vm.loadAll = loadAll;
    vm.loadPage = loadPage;
    vm.apiUrl = undefined;
    vm.page = 0;

    ////////////////

    function loadAll() {
      vm.apiUrl = $cookies.get("apiUrl");
      if (vm.apiUrl) {
        TvFeed.query({apiUrl: vm.apiUrl}, function (result) {
          allItems = result;
          addFeedItems();
        });
      }
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

    function clearFilter() {
      if (vm.filter && vm.filter.showTitle.length) {
        if (cachedItems.length) {
          vm.feedItems = cachedItems.slice(0);
          cachedItems = [];
        }
        vm.filter.showTitle = undefined;
      }
    }

    function filterActive() {
      if (vm.filter.showTitle.length > 1 && !cachedItems.length) {
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

