'use strict';

angular.module('traqtionApp')
    .controller('ProductStatusDetailController', function ($scope, $rootScope, $stateParams, entity, ProductStatus) {
        $scope.productStatus = entity;
        $scope.load = function (id) {
            ProductStatus.get({id: id}, function(result) {
                $scope.productStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:productStatusUpdate', function(event, result) {
            $scope.productStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
