'use strict';

angular.module('traqtionApp')
    .controller('SupplierController', function ($scope, $state, Supplier, SupplierSearch) {

        $scope.suppliers = [];
        $scope.loadAll = function() {
            Supplier.query(function(result) {
               $scope.suppliers = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SupplierSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.suppliers = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.supplier = {
                id: null
            };
        };
    });
