'use strict';

angular.module('natureseyesApp')
    .controller('BroadcastDetailController', function ($scope, $rootScope, $stateParams, entity, Broadcast) {
        $scope.broadcast = entity;
        $scope.load = function (id) {
            Broadcast.get({id: id}, function(result) {
                $scope.broadcast = result;
            });
        };
        var unsubscribe = $rootScope.$on('natureseyesApp:broadcastUpdate', function(event, result) {
            $scope.broadcast = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
