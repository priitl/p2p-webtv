(function () {
  'use strict';

  angular
    .module('wtvApp')
    .controller('LanguageController', LanguageController)
    .filter('findLanguageFromKey', findLanguageFromKey);

  function LanguageController($translate, Language, tmhDynamicLocale) {
    var lang = this;

    lang.changeLanguage = changeLanguage;
    lang.languages = [];

    activate();

    ////////////////

    function changeLanguage(languageKey) {
      $translate.use(languageKey);
      tmhDynamicLocale.set(languageKey);
    }

    function activate() {
      Language.getAll().then(function (languages) {
        lang.languages = languages;
      });
    }
  }

  function findLanguageFromKey() {
    return findLanguageFromKeyFilter;

    ////////////////

    function findLanguageFromKeyFilter(lang) {
      return {
        "ca": "Català",
        "da": "Dansk",
        "de": "Deutsch",
        "en": "English",
        "es": "Español",
        "fr": "Français",
        "ta": "தமிழ்",
        "hu": "Magyar",
        "it": "Italiano",
        "ja": "日本語",
        "ko": "한국어",
        "nl": "Nederlands",
        "pl": "Polski",
        "pt-br": "Português (Brasil)",
        "pt-pt": "Português",
        "ro": "Română",
        "ru": "Русский",
        "sv": "Svenska",
        "gl": "Galego",
        "tr": "Türkçe",
        "zh-cn": "中文（简体）",
        "zh-tw": "繁體中文"
      }[lang];
    }
  }

})();
