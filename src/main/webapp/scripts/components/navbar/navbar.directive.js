(function () {
  'use strict';

  angular
    .module('maurusApp')
    .directive('sideNavigation', sideNavigation)
    .directive('miniNavbar', miniNavbar);

  function sideNavigation($timeout) {
    return {
      restrict: 'A',
      link: link
    };

    function link(scope, element) {
      $timeout(function () {
        element.metisMenu();
      });
    }
  }

  function miniNavbar() {
    return {
      bindToController: true,
      controller: MinimizeNavbarController,
      controllerAs: 'vm',
      restrict: 'A',
      templateUrl: 'scripts/components/navbar/mini-navbar.directive.html'
    };
  }

  function MinimizeNavbarController() {
    var vm = this;
    vm.minimize = function () {
      angular.element('body').toggleClass("fixed-sidebar mini-navbar");
      if (!angular.element('body').hasClass('mini-navbar') || angular.element('body').hasClass('body-small')) {
        $('#side-menu').hide();
        setTimeout(function () {
          $('#side-menu').fadeIn(500);
        }, 100);
      } else if (angular.element('body').hasClass('fixed-sidebar')) {
        $('#side-menu').hide();
        setTimeout(function () {
          $('#side-menu').fadeIn(500);
        }, 300);
      } else {
        $('#side-menu').removeAttr('style');
      }
    };
  }

})();
