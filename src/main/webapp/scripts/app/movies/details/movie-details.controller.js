(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('MovieDetailsController', MovieDetailsController);

  function MovieDetailsController(MovieDetails, $stateParams) {
    var vm = this;

    vm.movie = {};

    activate();

    ////////////////

    function activate() {
      MovieDetails.get({'tmdbId': $stateParams.tmdbId}, function (result) {
        vm.movie = result;
      });
    }

  }

})();

