'use strict';

angular.module('traqtionApp')
    .controller('UserBusinessRoleController', function ($scope, $state, UserBusinessRole, UserBusinessRoleSearch) {

        $scope.userBusinessRoles = [];
        $scope.loadAll = function() {
            UserBusinessRole.query(function(result) {
               $scope.userBusinessRoles = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            UserBusinessRoleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userBusinessRoles = result;
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
            $scope.userBusinessRole = {
                isActive: null,
                id: null
            };
        };
    });
