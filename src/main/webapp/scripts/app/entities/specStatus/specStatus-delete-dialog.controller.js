'use strict';

angular.module('traqtionApp')
	.controller('SpecStatusDeleteController', function($scope, $uibModalInstance, entity, SpecStatus) {

        $scope.specStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SpecStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
