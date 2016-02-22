'use strict';

angular.module('traqtionApp')
	.controller('BusinessRelationshipTypeDeleteController', function($scope, $uibModalInstance, entity, BusinessRelationshipType) {

        $scope.businessRelationshipType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BusinessRelationshipType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
