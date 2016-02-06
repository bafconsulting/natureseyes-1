'use strict';

angular.module('natureseyesApp')
    .controller('BroadcastsDetailController', function ($scope, $rootScope, $stateParams, entity, Broadcasts) {
        $scope.broadcasts = entity;
        $scope.load = function (id) {
            Broadcasts.get({id: id}, function(result) {
                $scope.broadcasts = result;
            });
        };
        var unsubscribe = $rootScope.$on('natureseyesApp:broadcastsUpdate', function(event, result) {
            $scope.broadcasts = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
