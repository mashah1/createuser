'use strict';

angular.module('traqtionApp')
    .controller('PermissionsController', function ($scope, $state, Permissions, PermissionsSearch) {

        $scope.permissionss = [];
        $scope.loadAll = function() {
            Permissions.query(function(result) {
               $scope.permissionss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PermissionsSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.permissionss = result;
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
            $scope.permissions = {
                attribute: null,
                id: null
            };
        };
    });
