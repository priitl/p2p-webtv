(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(uibPagerConfig);

  function uibPagerConfig(uibPagerConfig) {
    uibPagerConfig.itemsPerPage = 20;
    uibPagerConfig.previousText = '«';
    uibPagerConfig.nextText = '»';
  }

})();

