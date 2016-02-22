'use strict';

angular.module('traqtionApp')
	.controller('RolePermissionDeleteController', function($scope, $uibModalInstance, entity, RolePermission) {

        $scope.rolePermission = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            RolePermission.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
