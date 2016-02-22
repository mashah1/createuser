'use strict';

angular.module('traqtionApp').controller('SpecDocumentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpecDocument',
        function($scope, $stateParams, $uibModalInstance, entity, SpecDocument) {

        $scope.specDocument = entity;
        $scope.load = function(id) {
            SpecDocument.get({id : id}, function(result) {
                $scope.specDocument = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:specDocumentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.specDocument.id != null) {
                SpecDocument.update($scope.specDocument, onSaveSuccess, onSaveError);
            } else {
                SpecDocument.save($scope.specDocument, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
