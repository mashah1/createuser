'use strict';

angular.module('traqtionApp').controller('SupplierSiteDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'SupplierSite', 'Specification',
        function($scope, $stateParams, $uibModalInstance, $q, entity, SupplierSite, Specification) {

        $scope.supplierSite = entity;
        $scope.specifications = Specification.query({filter: 'suppliersite-is-null'});
        $q.all([$scope.supplierSite.$promise, $scope.specifications.$promise]).then(function() {
            if (!$scope.supplierSite.specification || !$scope.supplierSite.specification.id) {
                return $q.reject();
            }
            return Specification.get({id : $scope.supplierSite.specification.id}).$promise;
        }).then(function(specification) {
            $scope.specifications.push(specification);
        });
        $scope.load = function(id) {
            SupplierSite.get({id : id}, function(result) {
                $scope.supplierSite = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:supplierSiteUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.supplierSite.id != null) {
                SupplierSite.update($scope.supplierSite, onSaveSuccess, onSaveError);
            } else {
                SupplierSite.save($scope.supplierSite, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
