(function () {
  'use strict';

  angular
    .module('maurusApp')
    .factory('Principal', Principal);

  function Principal($q, Account, Tracker) {
    var _identity, _authenticated = false;

    return {
      authenticate: authenticate,
      hasAuthority: hasAuthority,
      hasAnyAuthority: hasAnyAuthority,
      identity: identity,
      isAuthenticated: isAuthenticated,
      isIdentityResolved: isIdentityResolved
    };

    ////////////////

    function authenticate(identity) {
      _identity = identity;
      _authenticated = identity !== null;
    }

    function isIdentityResolved() {
      return angular.isDefined(_identity);
    }

    function isAuthenticated() {
      return _authenticated;
    }

    function hasAuthority(authority) {
      if (!_authenticated) {
        return $q.when(false);
      }

      return this.identity().then(function (_id) {
        return _id.authorities && _id.authorities.indexOf(authority) !== -1;
      }, function (err) {
        return false;
      });
    }

    function hasAnyAuthority(authorities) {
      if (!_authenticated || !_identity || !_identity.authorities) {
        return false;
      }

      for (var i = 0; i < authorities.length; i++) {
        if (_identity.authorities.indexOf(authorities[i]) !== -1) {
          return true;
        }
      }

      return false;
    }

    function identity(force) {
      var deferred = $q.defer();

      if (force === true) {
        _identity = undefined;
      }

      if (angular.isDefined(_identity)) {
        deferred.resolve(_identity);
        return deferred.promise;
      }

      Account.get().$promise.then(function (account) {
        _identity = account.data;
        _authenticated = true;
        deferred.resolve(_identity);
        Tracker.connect();
      }).catch(function () {
        _identity = null;
        _authenticated = false;
        deferred.resolve(_identity);
      });
      return deferred.promise;
    }
  }

})();
