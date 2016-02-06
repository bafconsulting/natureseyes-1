'use strict';

angular.module('natureseyesApp').controller('BroadcastsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Broadcasts',
        function($scope, $stateParams, $uibModalInstance, entity, Broadcasts) {

        $scope.broadcasts = entity;
        $scope.load = function(id) {
            Broadcasts.get({id : id}, function(result) {
                $scope.broadcasts = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('natureseyesApp:broadcastsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.broadcasts.id != null) {
                Broadcasts.update($scope.broadcasts, onSaveSuccess, onSaveError);
            } else {
                Broadcasts.save($scope.broadcasts, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
