(function () {
  'use strict';

  angular
    .module('wtvApp')
    .config(uibPagerConfig);

  function uibPagerConfig(uibPagerConfig) {
    uibPagerConfig.itemsPerPage = 20;
    uibPagerConfig.previousText = '«';
    uibPagerConfig.nextText = '»';
  }

})();

