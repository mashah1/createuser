'use strict';

angular.module('traqtionApp')
    .controller('RoleDetailController', function ($scope, $rootScope, $stateParams, entity, Role, RolePermission) {
        $scope.role = entity;
        $scope.load = function (id) {
            Role.get({id: id}, function(result) {
                $scope.role = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:roleUpdate', function(event, result) {
            $scope.role = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
