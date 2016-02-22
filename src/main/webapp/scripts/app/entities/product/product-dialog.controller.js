'use strict';

angular.module('traqtionApp').controller('ProductDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Product', 'ProductType', 'Brand', 'PrimarySpec', 'ProductStatus',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Product, ProductType, Brand, PrimarySpec, ProductStatus) {

        $scope.product = entity;
        $scope.producttypes = ProductType.query({filter: 'product-is-null'});
        $q.all([$scope.product.$promise, $scope.producttypes.$promise]).then(function() {
            if (!$scope.product.productType || !$scope.product.productType.id) {
                return $q.reject();
            }
            return ProductType.get({id : $scope.product.productType.id}).$promise;
        }).then(function(productType) {
            $scope.producttypes.push(productType);
        });
        $scope.brands = Brand.query();
        $scope.primaryspecs = PrimarySpec.query({filter: 'product-is-null'});
        $q.all([$scope.product.$promise, $scope.primaryspecs.$promise]).then(function() {
            if (!$scope.product.primarySpec || !$scope.product.primarySpec.id) {
                return $q.reject();
            }
            return PrimarySpec.get({id : $scope.product.primarySpec.id}).$promise;
        }).then(function(primarySpec) {
            $scope.primaryspecs.push(primarySpec);
        });
        $scope.productstatuss = ProductStatus.query({filter: 'product-is-null'});
        $q.all([$scope.product.$promise, $scope.productstatuss.$promise]).then(function() {
            if (!$scope.product.productStatus || !$scope.product.productStatus.id) {
                return $q.reject();
            }
            return ProductStatus.get({id : $scope.product.productStatus.id}).$promise;
        }).then(function(productStatus) {
            $scope.productstatuss.push(productStatus);
        });
        $scope.load = function(id) {
            Product.get({id : id}, function(result) {
                $scope.product = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:productUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.product.id != null) {
                Product.update($scope.product, onSaveSuccess, onSaveError);
            } else {
                Product.save($scope.product, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
