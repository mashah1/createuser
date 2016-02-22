'use strict';

angular.module('traqtionApp').controller('BrandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Brand',
        function($scope, $stateParams, $uibModalInstance, entity, Brand) {

        $scope.brand = entity;
        $scope.load = function(id) {
            Brand.get({id : id}, function(result) {
                $scope.brand = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:brandUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.brand.id != null) {
                Brand.update($scope.brand, onSaveSuccess, onSaveError);
            } else {
                Brand.save($scope.brand, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
