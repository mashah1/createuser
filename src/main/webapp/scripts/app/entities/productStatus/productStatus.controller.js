'use strict';

angular.module('traqtionApp')
    .controller('ProductStatusController', function ($scope, $state, ProductStatus, ProductStatusSearch) {

        $scope.productStatuss = [];
        $scope.loadAll = function() {
            ProductStatus.query(function(result) {
               $scope.productStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productStatuss = result;
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
            $scope.productStatus = {
                name: null,
                id: null
            };
        };
    });
