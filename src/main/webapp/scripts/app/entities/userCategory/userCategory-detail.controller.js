'use strict';

angular.module('traqtionApp')
    .controller('UserCategoryDetailController', function ($scope, $rootScope, $stateParams, entity, UserCategory, Category, UserBusinessRole) {
        $scope.userCategory = entity;
        $scope.load = function (id) {
            UserCategory.get({id: id}, function(result) {
                $scope.userCategory = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:userCategoryUpdate', function(event, result) {
            $scope.userCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
