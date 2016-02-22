'use strict';

angular.module('traqtionApp')
    .controller('UserCategoryController', function ($scope, $state, UserCategory, UserCategorySearch) {

        $scope.userCategorys = [];
        $scope.loadAll = function() {
            UserCategory.query(function(result) {
               $scope.userCategorys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            UserCategorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userCategorys = result;
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
            $scope.userCategory = {
                id: null
            };
        };
    });
