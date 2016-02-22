'use strict';

angular.module('traqtionApp')
    .controller('RoleController', function ($scope, $state, Role, RoleSearch) {

        $scope.roles = [];
        $scope.loadAll = function() {
            Role.query(function(result) {
               $scope.roles = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            RoleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.roles = result;
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
            $scope.role = {
                name: null,
                description: null,
                id: null
            };
        };
    });
