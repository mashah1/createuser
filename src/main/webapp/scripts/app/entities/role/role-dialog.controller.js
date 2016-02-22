'use strict';

angular.module('traqtionApp').controller('RoleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Role', 'RolePermission',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Role, RolePermission) {

        $scope.role = entity;
        $scope.rolepermissions = RolePermission.query({filter: 'role-is-null'});
        $q.all([$scope.role.$promise, $scope.rolepermissions.$promise]).then(function() {
            if (!$scope.role.rolePermission || !$scope.role.rolePermission.id) {
                return $q.reject();
            }
            return RolePermission.get({id : $scope.role.rolePermission.id}).$promise;
        }).then(function(rolePermission) {
            $scope.rolepermissions.push(rolePermission);
        });
        $scope.load = function(id) {
            Role.get({id : id}, function(result) {
                $scope.role = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:roleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.role.id != null) {
                Role.update($scope.role, onSaveSuccess, onSaveError);
            } else {
                Role.save($scope.role, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
