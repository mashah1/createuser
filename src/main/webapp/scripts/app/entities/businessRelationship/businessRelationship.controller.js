'use strict';

angular.module('traqtionApp')
    .controller('BusinessRelationshipController', function ($scope, $state, BusinessRelationship, BusinessRelationshipSearch) {

        $scope.businessRelationships = [];
        $scope.loadAll = function() {
            BusinessRelationship.query(function(result) {
               $scope.businessRelationships = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            BusinessRelationshipSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.businessRelationships = result;
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
            $scope.businessRelationship = {
                id: null
            };
        };
    });
