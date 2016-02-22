'use strict';

angular.module('traqtionApp').controller('PrimarySpecDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrimarySpec',
        function($scope, $stateParams, $uibModalInstance, entity, PrimarySpec) {

        $scope.primarySpec = entity;
        $scope.load = function(id) {
            PrimarySpec.get({id : id}, function(result) {
                $scope.primarySpec = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:primarySpecUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.primarySpec.id != null) {
                PrimarySpec.update($scope.primarySpec, onSaveSuccess, onSaveError);
            } else {
                PrimarySpec.save($scope.primarySpec, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
