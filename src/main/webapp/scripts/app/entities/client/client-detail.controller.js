'use strict';

angular.module('traqtionApp')
    .controller('ClientDetailController', function ($scope, $rootScope, $stateParams, entity, Client, Business) {
        $scope.client = entity;
        $scope.load = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:clientUpdate', function(event, result) {
            $scope.client = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
