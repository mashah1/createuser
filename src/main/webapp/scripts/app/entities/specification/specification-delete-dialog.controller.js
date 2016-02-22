'use strict';

angular.module('traqtionApp')
	.controller('SpecificationDeleteController', function($scope, $uibModalInstance, entity, Specification) {

        $scope.specification = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Specification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
