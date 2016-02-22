'use strict';

angular.module('traqtionApp')
    .controller('SupplierSiteDetailController', function ($scope, $rootScope, $stateParams, entity, SupplierSite, Specification) {
        $scope.supplierSite = entity;
        $scope.load = function (id) {
            SupplierSite.get({id: id}, function(result) {
                $scope.supplierSite = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:supplierSiteUpdate', function(event, result) {
            $scope.supplierSite = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
