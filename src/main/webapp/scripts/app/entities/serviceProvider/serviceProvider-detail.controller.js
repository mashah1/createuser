'use strict';

angular.module('traqtionApp')
    .controller('ServiceProviderDetailController', function ($scope, $rootScope, $stateParams, entity, ServiceProvider, Business) {
        $scope.serviceProvider = entity;
        $scope.load = function (id) {
            ServiceProvider.get({id: id}, function(result) {
                $scope.serviceProvider = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:serviceProviderUpdate', function(event, result) {
            $scope.serviceProvider = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
