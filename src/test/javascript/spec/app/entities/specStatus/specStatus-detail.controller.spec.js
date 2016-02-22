'use strict';

describe('Controller Tests', function() {

    describe('SpecStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSpecStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSpecStatus = jasmine.createSpy('MockSpecStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SpecStatus': MockSpecStatus
            };
            createController = function() {
                $injector.get('$controller')("SpecStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:specStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
