'use strict';

angular.module('traqtionApp')
	.controller('PrimarySpecDeleteController', function($scope, $uibModalInstance, entity, PrimarySpec) {

        $scope.primarySpec = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PrimarySpec.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
