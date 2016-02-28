'use strict';

angular.module('natureseyesApp').controller('YoutubeBroadcastDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'YoutubeBroadcast',
        function($scope, $stateParams, $uibModalInstance, entity, YoutubeBroadcast) {

        $scope.youtubeBroadcast = entity;
        $scope.load = function(id) {
            YoutubeBroadcast.get({id : id}, function(result) {
                $scope.youtubeBroadcast = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('natureseyesApp:youtubeBroadcastUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.youtubeBroadcast.id != null) {
                YoutubeBroadcast.update($scope.youtubeBroadcast, onSaveSuccess, onSaveError);
            } else {
                YoutubeBroadcast.save($scope.youtubeBroadcast, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForScheduledStartTime = {};

        $scope.datePickerForScheduledStartTime.status = {
            opened: false
        };

        $scope.datePickerForScheduledStartTimeOpen = function($event) {
            $scope.datePickerForScheduledStartTime.status.opened = true;
        };
        $scope.datePickerForScheduledEndTime = {};

        $scope.datePickerForScheduledEndTime.status = {
            opened: false
        };

        $scope.datePickerForScheduledEndTimeOpen = function($event) {
            $scope.datePickerForScheduledEndTime.status.opened = true;
        };
}]);
