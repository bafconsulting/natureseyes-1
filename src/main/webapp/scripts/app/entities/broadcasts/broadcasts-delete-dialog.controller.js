'use strict';

angular.module('natureseyesApp')
	.controller('BroadcastsDeleteController', function($scope, $uibModalInstance, entity, Broadcasts) {

        $scope.broadcasts = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Broadcasts.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
