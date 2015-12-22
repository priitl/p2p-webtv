(function () {
  'use strict';

  angular
    .module('wtvApp')
    .filter('prettyBytes', prettyBytes);

  function prettyBytes() {
    return prettyBytesFilter;

    ////////////////

    function prettyBytesFilter(input) {
      if (!_.isNumber(input)) {
        throw new TypeError('Expected a number');
      }

      var units = ['B', 'kB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
      var negative = input < 0;
      input = negative ? -input : input;

      if (input < 1) {
        return (negative ? '-' : '') + input + ' B';
      }

      var exponent = Math.min(Math.floor(Math.log(input) / Math.log(1000)), units.length - 1);
      input = Number((input / Math.pow(1000, exponent)).toFixed(2));
      var unit = units[exponent];
      return (negative ? '-' : '') + input + ' ' + unit;
    }
  }

})();
