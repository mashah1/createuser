'use strict';

angular.module('traqtionApp')
    .controller('UserBusinessRoleDetailController', function ($scope, $rootScope, $stateParams, entity, UserBusinessRole, User, Role, Business) {
        $scope.userBusinessRole = entity;
        $scope.load = function (id) {
            UserBusinessRole.get({id: id}, function(result) {
                $scope.userBusinessRole = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:userBusinessRoleUpdate', function(event, result) {
            $scope.userBusinessRole = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
