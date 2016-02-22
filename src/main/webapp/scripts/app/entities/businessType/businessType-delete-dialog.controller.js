'use strict';

angular.module('traqtionApp')
	.controller('BusinessTypeDeleteController', function($scope, $uibModalInstance, entity, BusinessType) {

        $scope.businessType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BusinessType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
