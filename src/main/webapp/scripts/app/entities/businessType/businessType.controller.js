'use strict';

angular.module('traqtionApp')
    .controller('BusinessTypeController', function ($scope, $state, BusinessType, BusinessTypeSearch) {

        $scope.businessTypes = [];
        $scope.loadAll = function() {
            BusinessType.query(function(result) {
               $scope.businessTypes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            BusinessTypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.businessTypes = result;
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
            $scope.businessType = {
                name: null,
                id: null
            };
        };
    });
