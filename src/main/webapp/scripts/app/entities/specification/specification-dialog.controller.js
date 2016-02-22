'use strict';

angular.module('traqtionApp').controller('SpecificationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Specification', 'PrimarySpec', 'SpecStatus', 'SpecDocument',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Specification, PrimarySpec, SpecStatus, SpecDocument) {

        $scope.specification = entity;
        $scope.primaryspecs = PrimarySpec.query();
        $scope.specstatuss = SpecStatus.query({filter: 'specification-is-null'});
        $q.all([$scope.specification.$promise, $scope.specstatuss.$promise]).then(function() {
            if (!$scope.specification.specStatus || !$scope.specification.specStatus.id) {
                return $q.reject();
            }
            return SpecStatus.get({id : $scope.specification.specStatus.id}).$promise;
        }).then(function(specStatus) {
            $scope.specstatuss.push(specStatus);
        });
        $scope.documents = SpecDocument.query({filter: 'specification-is-null'});
        $q.all([$scope.specification.$promise, $scope.documents.$promise]).then(function() {
            if (!$scope.specification.document || !$scope.specification.document.id) {
                return $q.reject();
            }
            return SpecDocument.get({id : $scope.specification.document.id}).$promise;
        }).then(function(document) {
            $scope.documents.push(document);
        });
        $scope.load = function(id) {
            Specification.get({id : id}, function(result) {
                $scope.specification = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:specificationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.specification.id != null) {
                Specification.update($scope.specification, onSaveSuccess, onSaveError);
            } else {
                Specification.save($scope.specification, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
