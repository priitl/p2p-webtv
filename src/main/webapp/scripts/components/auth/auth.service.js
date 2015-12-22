(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('Auth', Auth);

  function Auth($rootScope, $state, $q, $translate, Principal, AuthServerProvider, Account, Register, Activate, Password,
                PasswordResetInit, PasswordResetFinish, Tracker) {
    return {
      activateAccount: activateAccount,
      authorize: authorize,
      changePassword: changePassword,
      createAccount: createAccount,
      login: login,
      logout: logout,
      resetPasswordInit: resetPasswordInit,
      resetPasswordFinish: resetPasswordFinish,
      updateAccount: updateAccount
    };

    ////////////////

    function login(credentials, callback) {
      var cb = callback || angular.noop;
      var deferred = $q.defer();

      AuthServerProvider.login(credentials).then(function (data) {
        Principal.identity(true).then(function (account) {
          $rootScope.$emit('::userChanged', account);
          $translate.use(account.langKey).then(function () {
            $translate.refresh();
          });
          Tracker.sendActivity();
          deferred.resolve(data);
        });
        return cb();
      }).catch(function (err) {
        this.logout();
        deferred.reject(err);
        return cb(err);
      }.bind(this));

      return deferred.promise;
    }

    function logout() {
      AuthServerProvider.logout();
      Principal.authenticate(null);
      $rootScope.previousStateName = undefined;
      $rootScope.previousStateNameParams = undefined;
      $rootScope.$emit('::userChanged', null);
    }

    function authorize(force) {
      return Principal.identity(force).then(function () {
        var isAuthenticated = Principal.isAuthenticated();

        if (isAuthenticated && $rootScope.toState.parent === 'account' &&
            ($rootScope.toState.name === 'login' || $rootScope.toState.name === 'register')) {
          $state.go('home');
        }

        if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0
            && !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
          if (isAuthenticated) {
            $state.go('accessdenied');
          } else {
            $rootScope.previousStateName = $rootScope.toState;
            $rootScope.previousStateNameParams = $rootScope.toStateParams;
            $state.go('login');
          }
        }
      });
    }

    function createAccount(account, callback) {
      var cb = callback || angular.noop;
      return Register.save(account, function () {
        return cb(account);
      }, function (err) {
        this.logout();
        return cb(err);
      }.bind(this)).$promise;
    }

    function updateAccount(account, callback) {
      var cb = callback || angular.noop;
      return Account.save(account, function () {
        $rootScope.$emit('::userUpdated', account);
        return cb(account);
      }, function (err) {
        return cb(err);
      }.bind(this)).$promise;
    }

    function activateAccount(key, callback) {
      var cb = callback || angular.noop;
      return Activate.get(key, function (response) {
        return cb(response);
      }, function (err) {
        return cb(err);
      }.bind(this)).$promise;
    }

    function changePassword(newPassword, callback) {
      var cb = callback || angular.noop;
      return Password.save(newPassword, function () {
        return cb();
      }, function (err) {
        return cb(err);
      }).$promise;
    }

    function resetPasswordInit(mail, callback) {
      var cb = callback || angular.noop;
      return PasswordResetInit.save(mail, function () {
        return cb();
      }, function (err) {
        return cb(err);
      }).$promise;
    }

    function resetPasswordFinish(keyAndPassword, callback) {
      var cb = callback || angular.noop;
      return PasswordResetFinish.save(keyAndPassword, function () {
        return cb();
      }, function (err) {
        return cb(err);
      }).$promise;
    }
  }

})();
