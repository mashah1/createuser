'use strict';

angular.module('traqtionApp')
	.controller('BusinessDeleteController', function($scope, $uibModalInstance, entity, Business) {

        $scope.business = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Business.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
