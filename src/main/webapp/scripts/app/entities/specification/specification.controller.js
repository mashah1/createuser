'use strict';

angular.module('traqtionApp')
    .controller('SpecificationController', function ($scope, $state, Specification, SpecificationSearch) {

        $scope.specifications = [];
        $scope.loadAll = function() {
            Specification.query(function(result) {
               $scope.specifications = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SpecificationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.specifications = result;
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
            $scope.specification = {
                attribute: null,
                attribute1: null,
                version: null,
                id: null
            };
        };
    });
