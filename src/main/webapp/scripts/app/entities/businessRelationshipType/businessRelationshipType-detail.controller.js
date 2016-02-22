'use strict';

angular.module('traqtionApp')
    .controller('BusinessRelationshipTypeDetailController', function ($scope, $rootScope, $stateParams, entity, BusinessRelationshipType) {
        $scope.businessRelationshipType = entity;
        $scope.load = function (id) {
            BusinessRelationshipType.get({id: id}, function(result) {
                $scope.businessRelationshipType = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:businessRelationshipTypeUpdate', function(event, result) {
            $scope.businessRelationshipType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
