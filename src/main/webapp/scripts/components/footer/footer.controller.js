(function () {
  'use strict';

  angular
    .module('wtvApp')
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
