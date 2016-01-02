(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('TvDetailsController', TvDetailsController);

  function TvDetailsController(TvDetails, TvSeason, $stateParams, $cookies) {
    var vm = this;

    vm.show = {};
    vm.defaultCastItemLimit = 6;
    vm.toggleFullCast = toggleFullCast;
    vm.changeSeason = changeSeason;
    vm.getSeasons = getSeasons;
    vm.isTorrentItemMargined = isTorrentItemMargined;
    vm.apiUrl = undefined;

    activate();

    ////////////////

    function activate() {
      vm.apiUrl = $cookies.get("apiUrl");
      vm.castItemLimit = vm.defaultCastItemLimit;
      TvDetails.get({'tmdbId': $stateParams.tmdbId}, function (result) {
        vm.show = result;
      });
    }

    function changeSeason(seasonNumber) {
      TvSeason.get({'tmdbId': $stateParams.tmdbId, 'seasonNumber': seasonNumber, apiUrl: vm.apiUrl}, function (result) {
        vm.season = result;
      });
    }

    function getSeasons() {
      return new Array(vm.show.numberOfSeasons);
    }

    function toggleFullCast() {
      vm.castItemLimit = vm.castItemLimit === vm.defaultCastItemLimit ? vm.show.cast.length : vm.defaultCastItemLimit;
    }

    function isTorrentItemMargined() {
      if (angular.isUndefined(vm.season.episodes)) {
        return false
      }
      var episodesWithoutTorrents = _.where(vm.season.episodes, {magnetUri: null});
      return angular.isDefined(episodesWithoutTorrents) && episodesWithoutTorrents.length > 0 &&
             episodesWithoutTorrents.length !== vm.season.episodes.length;
    }

  }

})();

