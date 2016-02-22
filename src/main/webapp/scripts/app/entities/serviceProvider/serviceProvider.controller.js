'use strict';

angular.module('traqtionApp')
    .controller('ServiceProviderController', function ($scope, $state, ServiceProvider, ServiceProviderSearch) {

        $scope.serviceProviders = [];
        $scope.loadAll = function() {
            ServiceProvider.query(function(result) {
               $scope.serviceProviders = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ServiceProviderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.serviceProviders = result;
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
            $scope.serviceProvider = {
                id: null
            };
        };
    });
