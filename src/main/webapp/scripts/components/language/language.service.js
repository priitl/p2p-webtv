(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('Language', Language)
    .constant('LANGUAGES', ['en', 'fr']);

  function Language($q, $translate, LANGUAGES) {
    return {
      getCurrent: getCurrent,
      getAll: getAll
    };

    ////////////////

    function getCurrent() {
      var deferred = $q.defer();
      var language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

      if (angular.isUndefined(language)) {
        language = 'en';
      }

      deferred.resolve(language);
      return deferred.promise;
    }

    function getAll() {
      var deferred = $q.defer();
      deferred.resolve(LANGUAGES);
      return deferred.promise;
    }
  }

})();
