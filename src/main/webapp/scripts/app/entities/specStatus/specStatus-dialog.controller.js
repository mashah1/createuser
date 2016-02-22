'use strict';

angular.module('traqtionApp').controller('SpecStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpecStatus',
        function($scope, $stateParams, $uibModalInstance, entity, SpecStatus) {

        $scope.specStatus = entity;
        $scope.load = function(id) {
            SpecStatus.get({id : id}, function(result) {
                $scope.specStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:specStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.specStatus.id != null) {
                SpecStatus.update($scope.specStatus, onSaveSuccess, onSaveError);
            } else {
                SpecStatus.save($scope.specStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
