'use strict';

angular.module('traqtionApp').controller('SupplierDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Supplier', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Supplier, Business) {

        $scope.supplier = entity;
        $scope.businesss = Business.query({filter: 'supplier-is-null'});
        $q.all([$scope.supplier.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.supplier.business || !$scope.supplier.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.supplier.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            Supplier.get({id : id}, function(result) {
                $scope.supplier = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:supplierUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.supplier.id != null) {
                Supplier.update($scope.supplier, onSaveSuccess, onSaveError);
            } else {
                Supplier.save($scope.supplier, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
