'use strict';

describe('Controller Tests', function() {

    describe('PrimarySpec Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPrimarySpec;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPrimarySpec = jasmine.createSpy('MockPrimarySpec');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PrimarySpec': MockPrimarySpec
            };
            createController = function() {
                $injector.get('$controller')("PrimarySpecDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:primarySpecUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
