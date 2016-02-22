'use strict';

angular.module('traqtionApp').controller('CategoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Category', 'Product', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Category, Product, Business) {

        $scope.category = entity;
        $scope.products = Product.query({filter: 'category-is-null'});
        $q.all([$scope.category.$promise, $scope.products.$promise]).then(function() {
            if (!$scope.category.product || !$scope.category.product.id) {
                return $q.reject();
            }
            return Product.get({id : $scope.category.product.id}).$promise;
        }).then(function(product) {
            $scope.products.push(product);
        });
        $scope.businesss = Business.query({filter: 'category-is-null'});
        $q.all([$scope.category.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.category.business || !$scope.category.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.category.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            Category.get({id : id}, function(result) {
                $scope.category = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:categoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.category.id != null) {
                Category.update($scope.category, onSaveSuccess, onSaveError);
            } else {
                Category.save($scope.category, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
