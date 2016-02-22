'use strict';

describe('Controller Tests', function() {

    describe('Specification Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSpecification, MockPrimarySpec, MockSpecStatus, MockSpecDocument;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSpecification = jasmine.createSpy('MockSpecification');
            MockPrimarySpec = jasmine.createSpy('MockPrimarySpec');
            MockSpecStatus = jasmine.createSpy('MockSpecStatus');
            MockSpecDocument = jasmine.createSpy('MockSpecDocument');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Specification': MockSpecification,
                'PrimarySpec': MockPrimarySpec,
                'SpecStatus': MockSpecStatus,
                'SpecDocument': MockSpecDocument
            };
            createController = function() {
                $injector.get('$controller')("SpecificationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:specificationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
