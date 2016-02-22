'use strict';

angular.module('traqtionApp')
    .controller('RolePermissionController', function ($scope, $state, RolePermission, RolePermissionSearch) {

        $scope.rolePermissions = [];
        $scope.loadAll = function() {
            RolePermission.query(function(result) {
               $scope.rolePermissions = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            RolePermissionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.rolePermissions = result;
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
            $scope.rolePermission = {
                id: null
            };
        };
    });
