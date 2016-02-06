'use strict';

angular.module('natureseyesApp')
	.controller('BroadcastDeleteController', function($scope, $uibModalInstance, entity, Broadcast) {

        $scope.broadcast = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Broadcast.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
