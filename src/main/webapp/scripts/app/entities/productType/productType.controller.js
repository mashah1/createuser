'use strict';

angular.module('traqtionApp')
    .controller('ProductTypeController', function ($scope, $state, ProductType, ProductTypeSearch) {

        $scope.productTypes = [];
        $scope.loadAll = function() {
            ProductType.query(function(result) {
               $scope.productTypes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductTypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productTypes = result;
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
            $scope.productType = {
                name: null,
                description: null,
                id: null
            };
        };
    });
