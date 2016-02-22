'use strict';

angular.module('traqtionApp').controller('BusinessRelationshipTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BusinessRelationshipType',
        function($scope, $stateParams, $uibModalInstance, entity, BusinessRelationshipType) {

        $scope.businessRelationshipType = entity;
        $scope.load = function(id) {
            BusinessRelationshipType.get({id : id}, function(result) {
                $scope.businessRelationshipType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:businessRelationshipTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.businessRelationshipType.id != null) {
                BusinessRelationshipType.update($scope.businessRelationshipType, onSaveSuccess, onSaveError);
            } else {
                BusinessRelationshipType.save($scope.businessRelationshipType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
