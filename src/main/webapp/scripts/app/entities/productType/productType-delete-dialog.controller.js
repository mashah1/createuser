'use strict';

angular.module('traqtionApp')
	.controller('ProductTypeDeleteController', function($scope, $uibModalInstance, entity, ProductType) {

        $scope.productType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProductType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
