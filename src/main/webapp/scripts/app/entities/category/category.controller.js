'use strict';

angular.module('traqtionApp')
    .controller('CategoryController', function ($scope, $state, Category, CategorySearch) {

        $scope.categorys = [];
        $scope.loadAll = function() {
            Category.query(function(result) {
               $scope.categorys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CategorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.categorys = result;
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
            $scope.category = {
                id: null
            };
        };
    });
