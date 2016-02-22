'use strict';

angular.module('traqtionApp')
	.controller('SupplierSiteDeleteController', function($scope, $uibModalInstance, entity, SupplierSite) {

        $scope.supplierSite = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SupplierSite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
