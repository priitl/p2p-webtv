(function () {
  'use strict';

  var appDependencies = ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate', 'ui.bootstrap', 'ngAnimate',
                         'toastr', 'ngTouch', 'ngResource', 'ui.router', 'ngCookies', 'ngAria', 'ngCacheBuster',
                         'ngFileUpload', 'infinite-scroll', 'angular-loading-bar'];
  angular
    .module('maurusApp', appDependencies)
    .run(appRun)
    .config(appConfig)
    .config(toasterConfig)
    .config(['$urlMatcherFactoryProvider', urlMatcherConfig]);

  function appRun($rootScope, $window, $state, $translate, Language, Auth, Principal, ENV, VERSION) {
    var updateTitle = function (titleKey) {
      if (!titleKey && $state.$current.data && $state.$current.data.pageTitle) {
        titleKey = $state.$current.data.pageTitle;
      }
      $translate(titleKey || 'global.title').then(function (title) {
        $window.document.title = title;
      });
    };

    $rootScope.ENV = ENV;
    $rootScope.VERSION = VERSION;
    $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
      $rootScope.toState = toState;
      $rootScope.toStateParams = toStateParams;
      if (Principal.isIdentityResolved()) {
        Auth.authorize();
      }
      Language.getCurrent().then(function (language) {
        $translate.use(language);
      });

    });

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
      var titleKey = 'global.title';
      if (toState.name != 'login' && $rootScope.previousStateName) {
        $rootScope.previousStateName = fromState.name;
        $rootScope.previousStateParams = fromParams;
      }

      if (toState.data.pageTitle) {
        titleKey = toState.data.pageTitle;
      }
      updateTitle(titleKey);
    });

    $rootScope.$on('$translateChangeSuccess', function () {
      updateTitle();
    });

    $rootScope.back = function () {
      if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
        $state.go('home');
      } else {
        $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
      }
    };
  }

  function appConfig($stateProvider, $urlRouterProvider, $httpProvider, $translateProvider, $compileProvider,
                     tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {
    //enable CSRF
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

    //Cache everything except rest api requests
    httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

    $urlRouterProvider.otherwise('/');
    $stateProvider.state('site', {
      'abstract': true,
      views: {
        'navbar@': {
          templateUrl: 'scripts/components/navbar/navbar.html',
          controller: 'NavbarController',
          controllerAs: 'vm'
        },
        'header@': {
          templateUrl: 'scripts/components/header/header.html',
          controller: 'HeaderController'
        },
        'footer@': {
          templateUrl: 'scripts/components/footer/footer.html',
          controller: 'FooterController'
        }
      },
      resolve: {
        authorize: ['Auth', function (Auth) {
          return Auth.authorize();
        }],
        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
          $translatePartialLoader.addPart('global');
        }]
      }
    });

    $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|magnet):/);

    $httpProvider.interceptors.push('errorHandlerInterceptor');
    $httpProvider.interceptors.push('authExpiredInterceptor');
    $httpProvider.interceptors.push('notificationInterceptor');

    // Initialize angular-translate
    $translateProvider.useLoader('$translatePartialLoader', {
      urlTemplate: 'i18n/{lang}/{part}.json'
    });

    $translateProvider.preferredLanguage('en');
    $translateProvider.useCookieStorage();
    $translateProvider.useSanitizeValueStrategy('escaped');
    $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

    tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
    tmhDynamicLocaleProvider.useCookieStorage();
    tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');
  }

  function toasterConfig(toastrConfig) {
    angular.extend(toastrConfig, {
      "closeButton": true,
      "debug": false,
      "progressBar": true,
      "preventDuplicates": false,
      "positionClass": "toast-top-right",
      "onclick": null,
      "showDuration": "400",
      "hideDuration": "1000",
      "timeOut": "3000",
      "extendedTimeOut": "1000",
      "showEasing": "swing",
      "hideEasing": "linear",
      "showMethod": "fadeIn",
      "hideMethod": "fadeOut"
    });
  }

  function urlMatcherConfig($urlMatcherFactory) {
    $urlMatcherFactory.type('boolean', {
      name: 'boolean',
      decode: function (val) {
        return val == true ? true : val == "true"
      },
      encode: function (val) {
        return val ? 1 : 0;
      },
      equals: function (a, b) {
        return this.is(a) && a === b;
      },
      is: function (val) {
        return [true, false, 0, 1].indexOf(val) >= 0
      },
      pattern: /bool|true|0|1/
    });
  }

})();
