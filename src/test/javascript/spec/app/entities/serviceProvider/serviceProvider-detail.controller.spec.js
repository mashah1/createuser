'use strict';

describe('Controller Tests', function() {

    describe('ServiceProvider Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockServiceProvider, MockBusiness;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockServiceProvider = jasmine.createSpy('MockServiceProvider');
            MockBusiness = jasmine.createSpy('MockBusiness');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ServiceProvider': MockServiceProvider,
                'Business': MockBusiness
            };
            createController = function() {
                $injector.get('$controller')("ServiceProviderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:serviceProviderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
