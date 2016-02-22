'use strict';

angular.module('traqtionApp')
    .controller('BusinessTypeDetailController', function ($scope, $rootScope, $stateParams, entity, BusinessType, RolePermission, Business) {
        $scope.businessType = entity;
        $scope.load = function (id) {
            BusinessType.get({id: id}, function(result) {
                $scope.businessType = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:businessTypeUpdate', function(event, result) {
            $scope.businessType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
