'use strict';

angular.module('traqtionApp')
	.controller('PermissionsDeleteController', function($scope, $uibModalInstance, entity, Permissions) {

        $scope.permissions = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Permissions.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
