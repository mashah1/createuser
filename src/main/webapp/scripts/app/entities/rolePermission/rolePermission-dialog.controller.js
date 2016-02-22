'use strict';

angular.module('traqtionApp').controller('RolePermissionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'RolePermission',
        function($scope, $stateParams, $uibModalInstance, entity, RolePermission) {

        $scope.rolePermission = entity;
        $scope.load = function(id) {
            RolePermission.get({id : id}, function(result) {
                $scope.rolePermission = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:rolePermissionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.rolePermission.id != null) {
                RolePermission.update($scope.rolePermission, onSaveSuccess, onSaveError);
            } else {
                RolePermission.save($scope.rolePermission, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
