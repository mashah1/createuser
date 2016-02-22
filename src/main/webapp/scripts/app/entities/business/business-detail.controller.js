'use strict';

angular.module('traqtionApp')
    .controller('BusinessDetailController', function ($scope, $rootScope, $stateParams, entity, Business) {
        $scope.business = entity;
        $scope.load = function (id) {
            Business.get({id: id}, function(result) {
                $scope.business = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:businessUpdate', function(event, result) {
            $scope.business = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
