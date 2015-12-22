(function () {
  'use strict';

  angular
    .module('wtvApp')
    .directive('jhSocial', jhSocial);

  function jhSocial($translatePartialLoader, $translate, $filter, SocialService) {
    return {
      restrict: 'E',
      link: link,
      templateUrl: 'scripts/app/account/social/directive/social.html',
      scope: {
        provider: "@ngProvider"
      }
    };

    function link(scope) {
      $translatePartialLoader.addPart('social');
      $translate.refresh();

      scope.label = $filter('capitalize')(scope.provider);
      scope.providerSetting = SocialService.getProviderSetting(scope.provider);
      scope.providerURL = SocialService.getProviderURL(scope.provider);
      scope.csrf = SocialService.getCSRF();
    }
  }

})();

