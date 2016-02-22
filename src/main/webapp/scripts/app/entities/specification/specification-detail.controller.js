'use strict';

angular.module('traqtionApp')
    .controller('SpecificationDetailController', function ($scope, $rootScope, $stateParams, entity, Specification, PrimarySpec, SpecStatus, SpecDocument) {
        $scope.specification = entity;
        $scope.load = function (id) {
            Specification.get({id: id}, function(result) {
                $scope.specification = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:specificationUpdate', function(event, result) {
            $scope.specification = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
