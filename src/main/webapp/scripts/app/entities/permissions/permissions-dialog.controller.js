'use strict';

angular.module('traqtionApp').controller('PermissionsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Permissions', 'RolePermission',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Permissions, RolePermission) {

        $scope.permissions = entity;
        $scope.rolepermissions = RolePermission.query({filter: 'permissions-is-null'});
        $q.all([$scope.permissions.$promise, $scope.rolepermissions.$promise]).then(function() {
            if (!$scope.permissions.rolePermission || !$scope.permissions.rolePermission.id) {
                return $q.reject();
            }
            return RolePermission.get({id : $scope.permissions.rolePermission.id}).$promise;
        }).then(function(rolePermission) {
            $scope.rolepermissions.push(rolePermission);
        });
        $scope.load = function(id) {
            Permissions.get({id : id}, function(result) {
                $scope.permissions = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:permissionsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.permissions.id != null) {
                Permissions.update($scope.permissions, onSaveSuccess, onSaveError);
            } else {
                Permissions.save($scope.permissions, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
