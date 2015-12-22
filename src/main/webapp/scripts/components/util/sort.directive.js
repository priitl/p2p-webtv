(function () {
  'use strict';

  angular
    .module('wtvApp')
    .directive('wtvSort', wtvSort)
    .directive('wtvSortBy', wtvSortBy);

  function wtvSort() {
    return {
      bindToController: true,
      controller: WtvSortController,
      controllerAs: 'vm',
      restrict: 'A',
      scope: {
        predicate: '=wtvSort',
        ascending: '=',
        callback: '&'
      }
    };
  }

  function WtvSortController($scope) {
    this.sort = function (field) {
      if (field !== $scope.predicate) {
        $scope.ascending = true;
      } else {
        $scope.ascending = !$scope.ascending;
      }
      $scope.predicate = field;
      $scope.$apply();
      $scope.callback();
    };

    this.applyClass = function (element) {
      var allThIcons = element.parent().find('span.glyphicon'),
        sortIcon = 'glyphicon-sort',
        sortAsc = 'glyphicon-sort-by-attributes',
        sortDesc = 'glyphicon-sort-by-attributes-alt',
        remove = sortIcon + ' ' + sortDesc,
        add = sortAsc,
        thisIcon = element.find('span.glyphicon');
      if (!$scope.ascending) {
        remove = sortIcon + ' ' + sortAsc;
        add = sortDesc;
      }
      allThIcons.removeClass(sortAsc + ' ' + sortDesc);
      allThIcons.addClass(sortIcon);
      thisIcon.removeClass(remove);
      thisIcon.addClass(add);
    }
  }

  function wtvSortBy() {
    return {
      restrict: 'A',
      scope: false,
      require: '^wtvSort',
      link: link
    };

    function link(scope, element, attrs, parentCtrl) {
      element.bind('click', function () {
        parentCtrl.sort(attrs.wtvSortBy);
        parentCtrl.applyClass(element);
      });
    }
  }

})();
