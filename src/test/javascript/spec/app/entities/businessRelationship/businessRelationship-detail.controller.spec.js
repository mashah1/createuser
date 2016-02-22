'use strict';

describe('Controller Tests', function() {

    describe('BusinessRelationship Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBusinessRelationship, MockBusiness, MockBusinessRelationshipType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBusinessRelationship = jasmine.createSpy('MockBusinessRelationship');
            MockBusiness = jasmine.createSpy('MockBusiness');
            MockBusinessRelationshipType = jasmine.createSpy('MockBusinessRelationshipType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BusinessRelationship': MockBusinessRelationship,
                'Business': MockBusiness,
                'BusinessRelationshipType': MockBusinessRelationshipType
            };
            createController = function() {
                $injector.get('$controller')("BusinessRelationshipDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:businessRelationshipUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
