'use strict';

angular.module('traqtionApp')
	.controller('UserCategoryDeleteController', function($scope, $uibModalInstance, entity, UserCategory) {

        $scope.userCategory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
