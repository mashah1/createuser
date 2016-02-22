'use strict';

angular.module('traqtionApp').controller('ClientDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Client', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Client, Business) {

        $scope.client = entity;
        $scope.businesss = Business.query({filter: 'client-is-null'});
        $q.all([$scope.client.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.client.business || !$scope.client.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.client.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            Client.get({id : id}, function(result) {
                $scope.client = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:clientUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.client.id != null) {
                Client.update($scope.client, onSaveSuccess, onSaveError);
            } else {
                Client.save($scope.client, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
