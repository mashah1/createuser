'use strict';

angular.module('traqtionApp').controller('BusinessDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Business',
        function($scope, $stateParams, $uibModalInstance, entity, Business) {

        $scope.business = entity;
        $scope.load = function(id) {
            Business.get({id : id}, function(result) {
                $scope.business = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:businessUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.business.id != null) {
                Business.update($scope.business, onSaveSuccess, onSaveError);
            } else {
                Business.save($scope.business, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
