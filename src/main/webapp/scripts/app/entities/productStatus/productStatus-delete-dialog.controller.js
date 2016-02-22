'use strict';

angular.module('traqtionApp')
	.controller('ProductStatusDeleteController', function($scope, $uibModalInstance, entity, ProductStatus) {

        $scope.productStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ProductStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
