'use strict';

angular.module('traqtionApp').controller('ProductStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductStatus',
        function($scope, $stateParams, $uibModalInstance, entity, ProductStatus) {

        $scope.productStatus = entity;
        $scope.load = function(id) {
            ProductStatus.get({id : id}, function(result) {
                $scope.productStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:productStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.productStatus.id != null) {
                ProductStatus.update($scope.productStatus, onSaveSuccess, onSaveError);
            } else {
                ProductStatus.save($scope.productStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
