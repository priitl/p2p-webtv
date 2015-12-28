(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('MovieDetailsController', MovieDetailsController);

  function MovieDetailsController(MovieDetails, $stateParams) {
    var vm = this;

    vm.movie = {};
    vm.defaultCastItemLimit = 6;
    vm.toggleFullCast = toggleFullCast;

    activate();

    ////////////////

    function activate() {
      vm.castItemLimit = vm.defaultCastItemLimit;
      MovieDetails.get({'tmdbId': $stateParams.tmdbId}, function (result) {
        vm.movie = result;
      });
    }

    function toggleFullCast() {
      vm.castItemLimit = vm.castItemLimit === vm.defaultCastItemLimit ? vm.movie.cast.length : vm.defaultCastItemLimit;
    }

  }

})();

