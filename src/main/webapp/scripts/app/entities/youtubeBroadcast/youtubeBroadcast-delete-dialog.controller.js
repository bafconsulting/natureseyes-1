'use strict';

angular.module('natureseyesApp')
	.controller('YoutubeBroadcastDeleteController', function($scope, $uibModalInstance, entity, YoutubeBroadcast) {

        $scope.youtubeBroadcast = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            YoutubeBroadcast.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
