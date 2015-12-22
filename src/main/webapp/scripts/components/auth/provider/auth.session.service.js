(function () {
  'use strict';

  angular
    .module('wtvApp')
    .factory('AuthServerProvider', AuthServerProvider);

  function AuthServerProvider($http, localStorageService, Tracker) {
    return {
      getToken: getToken,
      hasValidToken: hasValidToken,
      login: login,
      logout: logout
    };

    ////////////////

    function getToken() {
      var token = localStorageService.get('token');
      return token;
    }

    function hasValidToken() {
      var token = this.getToken();
      return !!token;
    }

    function login(credentials) {
      var data = 'j_username=' + encodeURIComponent(credentials.username) +
                 '&j_password=' + encodeURIComponent(credentials.password) +
                 '&remember-me=' + credentials.rememberMe + '&submit=Login';
      return $http.post('api/authentication', data, {
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      }).success(function (response) {
        return response;
      });
    }

    function logout() {
      Tracker.disconnect();
      $http.post('api/logout').success(function (response) {
        localStorageService.clearAll();
        $http.get('api/account');
        return response;
      });
    }
  }

})();
