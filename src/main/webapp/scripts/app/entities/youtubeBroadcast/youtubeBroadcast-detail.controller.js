'use strict';

angular.module('natureseyesApp')
    .controller('YoutubeBroadcastDetailController', function ($scope, $rootScope, $stateParams, entity, YoutubeBroadcast) {
        $scope.youtubeBroadcast = entity;
        $scope.load = function (id) {
            YoutubeBroadcast.get({id: id}, function(result) {
                $scope.youtubeBroadcast = result;
            });
        };
        var unsubscribe = $rootScope.$on('natureseyesApp:youtubeBroadcastUpdate', function(event, result) {
            $scope.youtubeBroadcast = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
