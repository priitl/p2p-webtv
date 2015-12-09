(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvController', TvController);

  function TvController(Tv) {
    var vm = this;

    vm.popularTv = [];

    activate();

    ////////////////

    function activate() {
      Tv.get(function (result) {
        console.log(result);
        vm.popularTv = result.results;
      });
    }
  }

})();

