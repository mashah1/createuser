'use strict';

angular.module('traqtionApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, Product, Business) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
