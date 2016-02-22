'use strict';

angular.module('traqtionApp')
    .controller('BrandDetailController', function ($scope, $rootScope, $stateParams, entity, Brand) {
        $scope.brand = entity;
        $scope.load = function (id) {
            Brand.get({id: id}, function(result) {
                $scope.brand = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:brandUpdate', function(event, result) {
            $scope.brand = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
