'use strict';

angular.module('traqtionApp')
    .controller('RolePermissionDetailController', function ($scope, $rootScope, $stateParams, entity, RolePermission) {
        $scope.rolePermission = entity;
        $scope.load = function (id) {
            RolePermission.get({id: id}, function(result) {
                $scope.rolePermission = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:rolePermissionUpdate', function(event, result) {
            $scope.rolePermission = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
