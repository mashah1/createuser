'use strict';

angular.module('traqtionApp')
    .controller('PrimarySpecController', function ($scope, $state, PrimarySpec, PrimarySpecSearch) {

        $scope.primarySpecs = [];
        $scope.loadAll = function() {
            PrimarySpec.query(function(result) {
               $scope.primarySpecs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PrimarySpecSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.primarySpecs = result;
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
            $scope.primarySpec = {
                attribute: null,
                attribute1: null,
                id: null
            };
        };
    });
