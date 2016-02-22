'use strict';

angular.module('traqtionApp')
    .controller('BusinessController', function ($scope, $state, Business, BusinessSearch) {

        $scope.businesss = [];
        $scope.loadAll = function() {
            Business.query(function(result) {
               $scope.businesss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            BusinessSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.businesss = result;
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
            $scope.business = {
                name: null,
                displayName: null,
                address: null,
                description: null,
                id: null
            };
        };
    });
