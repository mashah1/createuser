'use strict';

angular.module('traqtionApp').controller('ManufacturerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Manufacturer', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Manufacturer, Business) {

        $scope.manufacturer = entity;
        $scope.businesss = Business.query({filter: 'manufacturer-is-null'});
        $q.all([$scope.manufacturer.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.manufacturer.business || !$scope.manufacturer.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.manufacturer.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            Manufacturer.get({id : id}, function(result) {
                $scope.manufacturer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:manufacturerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.manufacturer.id != null) {
                Manufacturer.update($scope.manufacturer, onSaveSuccess, onSaveError);
            } else {
                Manufacturer.save($scope.manufacturer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
