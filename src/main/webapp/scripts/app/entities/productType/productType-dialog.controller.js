'use strict';

angular.module('traqtionApp').controller('ProductTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductType',
        function($scope, $stateParams, $uibModalInstance, entity, ProductType) {

        $scope.productType = entity;
        $scope.load = function(id) {
            ProductType.get({id : id}, function(result) {
                $scope.productType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:productTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.productType.id != null) {
                ProductType.update($scope.productType, onSaveSuccess, onSaveError);
            } else {
                ProductType.save($scope.productType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
