(function () {
  'use strict';

  angular
    .module('maurusApp')
    .controller('TvFeedDialogController', TvFeedDialogController);

  function TvFeedDialogController($uibModalInstance, $stateParams, $filter, $rootScope, $interval, $translate, toastr) {
    var vm = this;
    var exampleMagnetUri = 'magnet:?xt=urn:btih:6a9759bffd5c0af65319979fb7832189f4f3c35d';
    vm.clear = clear;
    vm.episode = {};
    activate();

    ////////////////

    function activate() {
      toastr.warning($translate.instant('tv.feed.modal.noPeersWarningMessage'), {"timeOut": "10000", "preventDuplicates": true});
      $('#info').empty();
      vm.episode = $stateParams.episode;
      $rootScope.torrentClient.add(exampleMagnetUri, onTorrent);
    }

    function onTorrent(torrent) {

      function updateInfo() {
        $("#info").html(buildInfoHtml());
      }

      function buildInfoHtml() {
        var peers = torrent.swarm.wires.length;
        var progress = (100 * torrent.progress).toFixed(1) + '%';
        var downloadSpeed = $filter('prettyBytes')($rootScope.torrentClient.downloadSpeed()) + '/s';
        var uploadSpeed = $filter('prettyBytes')($rootScope.torrentClient.uploadSpeed()) + '/s';
        return wrapToDiv(getInfoColumn($translate.instant('tv.feed.modal.peers'), peers, ' | ') +
                         getInfoColumn($translate.instant('tv.feed.modal.progress'), progress, ' | ') +
                         getInfoColumn($translate.instant('tv.feed.modal.downloadSpeed'), downloadSpeed, ' | ') +
                         getInfoColumn($translate.instant('tv.feed.modal.uploadSpeed'), uploadSpeed, ''));
      }

      function wrapToDiv(content) {
        return '<div class="well">' + content + '</div>';
      }

      function getInfoColumn(title, content, separator) {
        return '<b>' + title + ':</b> ' + content + separator
      }

      $rootScope.updateTorrentInfo = $interval(updateInfo, 500);

      $('#player').empty();
      var file = torrent.files[0];
      file.appendTo('#player');
    }

    function clear() {
      $uibModalInstance.dismiss('cancel');
    }

  }

})();

