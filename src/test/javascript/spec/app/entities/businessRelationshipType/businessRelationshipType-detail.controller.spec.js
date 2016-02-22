'use strict';

describe('Controller Tests', function() {

    describe('BusinessRelationshipType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBusinessRelationshipType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBusinessRelationshipType = jasmine.createSpy('MockBusinessRelationshipType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BusinessRelationshipType': MockBusinessRelationshipType
            };
            createController = function() {
                $injector.get('$controller')("BusinessRelationshipTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:businessRelationshipTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
