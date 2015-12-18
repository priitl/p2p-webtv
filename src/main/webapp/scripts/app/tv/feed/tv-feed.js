(function () {
  'use strict';

  angular
    .module('maurusApp')
    .config(tvFeedConfig);

  function tvFeedConfig($stateProvider) {
    $stateProvider
      .state('tv-feed', {
        parent: 'tv',
        url: '/tv/feed',
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'global.menu.tv.feed'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/tv/feed/tv-feed.html',
            controller: 'TvFeedController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('tv');
            return $translate.refresh();
          }]
        }
      })
      .state('tv-feed.watch', {
        parent: 'tv-feed',
        url: '/{tvdbId}',
        data: {
          authorities: ['ROLE_USER']
        },
        params: {
          tvdbId: null,
          episode: null
        },
        onEnter: ['$stateParams', '$state', '$uibModal', '$rootScope', '$interval',
                  function ($stateParams, $state, $uibModal, $rootScope, $interval) {
          var onModalClose = function () {
            $interval.cancel($rootScope.updateTorrentInfo);
            $rootScope.updateTorrentInfo = undefined;
            angular.forEach($rootScope.torrentClient.torrents, function(torrent) {
              torrent.destroy();
            });
            $state.go('^')
          };
          $uibModal.open({
            templateUrl: 'scripts/app/tv/feed/tv-feed-dialog.html',
            controller: 'TvFeedDialogController',
            controllerAs: 'vm',
            size: 'lg',
            resolve: {
            mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('tv');
              return $translate.refresh();
            }]
          }
          }).result.then(onModalClose, onModalClose)
        }]
      });
  }

})();
