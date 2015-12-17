(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('FooterController', FooterController);

  function FooterController($anchorScroll) {
    var vm = this;

    vm.scrollToTop = scrollToTop;

    ////////////////

    function scrollToTop() {
      $anchorScroll();
    }
  }

})();
