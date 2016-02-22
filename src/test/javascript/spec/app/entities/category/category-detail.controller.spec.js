'use strict';

describe('Controller Tests', function() {

    describe('Category Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategory, MockProduct, MockBusiness;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategory = jasmine.createSpy('MockCategory');
            MockProduct = jasmine.createSpy('MockProduct');
            MockBusiness = jasmine.createSpy('MockBusiness');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Category': MockCategory,
                'Product': MockProduct,
                'Business': MockBusiness
            };
            createController = function() {
                $injector.get('$controller')("CategoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:categoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
