'use strict';

angular.module('traqtionApp')
    .controller('BrandController', function ($scope, $state, Brand, BrandSearch) {

        $scope.brands = [];
        $scope.loadAll = function() {
            Brand.query(function(result) {
               $scope.brands = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            BrandSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.brands = result;
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
            $scope.brand = {
                name: null,
                id: null
            };
        };
    });
