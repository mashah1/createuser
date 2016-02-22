'use strict';

angular.module('traqtionApp').controller('ServiceProviderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ServiceProvider', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, ServiceProvider, Business) {

        $scope.serviceProvider = entity;
        $scope.businesss = Business.query({filter: 'serviceprovider-is-null'});
        $q.all([$scope.serviceProvider.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.serviceProvider.business || !$scope.serviceProvider.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.serviceProvider.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            ServiceProvider.get({id : id}, function(result) {
                $scope.serviceProvider = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:serviceProviderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.serviceProvider.id != null) {
                ServiceProvider.update($scope.serviceProvider, onSaveSuccess, onSaveError);
            } else {
                ServiceProvider.save($scope.serviceProvider, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
