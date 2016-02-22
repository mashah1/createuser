'use strict';

angular.module('traqtionApp')
    .controller('PermissionsDetailController', function ($scope, $rootScope, $stateParams, entity, Permissions, RolePermission) {
        $scope.permissions = entity;
        $scope.load = function (id) {
            Permissions.get({id: id}, function(result) {
                $scope.permissions = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:permissionsUpdate', function(event, result) {
            $scope.permissions = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
