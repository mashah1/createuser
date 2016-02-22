'use strict';

describe('Controller Tests', function() {

    describe('Manufacturer Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockManufacturer, MockBusiness;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockManufacturer = jasmine.createSpy('MockManufacturer');
            MockBusiness = jasmine.createSpy('MockBusiness');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Manufacturer': MockManufacturer,
                'Business': MockBusiness
            };
            createController = function() {
                $injector.get('$controller')("ManufacturerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:manufacturerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
