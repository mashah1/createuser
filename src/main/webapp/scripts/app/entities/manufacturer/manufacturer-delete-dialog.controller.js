'use strict';

angular.module('traqtionApp')
	.controller('ManufacturerDeleteController', function($scope, $uibModalInstance, entity, Manufacturer) {

        $scope.manufacturer = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Manufacturer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
