'use strict';

angular.module('traqtionApp')
	.controller('BusinessRelationshipDeleteController', function($scope, $uibModalInstance, entity, BusinessRelationship) {

        $scope.businessRelationship = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BusinessRelationship.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
