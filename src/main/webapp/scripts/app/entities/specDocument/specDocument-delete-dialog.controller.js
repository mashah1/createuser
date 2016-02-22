'use strict';

angular.module('traqtionApp')
	.controller('SpecDocumentDeleteController', function($scope, $uibModalInstance, entity, SpecDocument) {

        $scope.specDocument = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SpecDocument.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
