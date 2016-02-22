'use strict';

angular.module('traqtionApp')
    .controller('PrimarySpecDetailController', function ($scope, $rootScope, $stateParams, entity, PrimarySpec) {
        $scope.primarySpec = entity;
        $scope.load = function (id) {
            PrimarySpec.get({id: id}, function(result) {
                $scope.primarySpec = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:primarySpecUpdate', function(event, result) {
            $scope.primarySpec = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
