'use strict';

angular.module('traqtionApp')
    .controller('BusinessRelationshipTypeController', function ($scope, $state, BusinessRelationshipType, BusinessRelationshipTypeSearch) {

        $scope.businessRelationshipTypes = [];
        $scope.loadAll = function() {
            BusinessRelationshipType.query(function(result) {
               $scope.businessRelationshipTypes = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            BusinessRelationshipTypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.businessRelationshipTypes = result;
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
            $scope.businessRelationshipType = {
                id: null
            };
        };
    });
