'use strict';

angular.module('traqtionApp')
	.controller('RoleDeleteController', function($scope, $uibModalInstance, entity, Role) {

        $scope.role = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Role.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
