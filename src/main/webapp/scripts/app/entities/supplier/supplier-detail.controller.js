'use strict';

angular.module('traqtionApp')
    .controller('SupplierDetailController', function ($scope, $rootScope, $stateParams, entity, Supplier, Business) {
        $scope.supplier = entity;
        $scope.load = function (id) {
            Supplier.get({id: id}, function(result) {
                $scope.supplier = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:supplierUpdate', function(event, result) {
            $scope.supplier = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
