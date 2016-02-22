'use strict';

angular.module('traqtionApp')
	.controller('UserBusinessRoleDeleteController', function($scope, $uibModalInstance, entity, UserBusinessRole) {

        $scope.userBusinessRole = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserBusinessRole.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
