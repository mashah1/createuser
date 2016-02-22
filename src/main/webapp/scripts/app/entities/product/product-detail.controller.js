'use strict';

angular.module('traqtionApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, ProductType, Brand, PrimarySpec, ProductStatus) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
