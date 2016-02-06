'use strict';

angular.module('natureseyesApp').controller('BroadcastDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Broadcast',
        function($scope, $stateParams, $uibModalInstance, entity, Broadcast) {

        $scope.broadcast = entity;
        $scope.load = function(id) {
            Broadcast.get({id : id}, function(result) {
                $scope.broadcast = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('natureseyesApp:broadcastUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.broadcast.id != null) {
                Broadcast.update($scope.broadcast, onSaveSuccess, onSaveError);
            } else {
                Broadcast.save($scope.broadcast, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
