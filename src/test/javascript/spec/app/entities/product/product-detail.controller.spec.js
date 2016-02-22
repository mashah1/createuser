'use strict';

describe('Controller Tests', function() {

    describe('Product Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProduct, MockProductType, MockBrand, MockPrimarySpec, MockProductStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProduct = jasmine.createSpy('MockProduct');
            MockProductType = jasmine.createSpy('MockProductType');
            MockBrand = jasmine.createSpy('MockBrand');
            MockPrimarySpec = jasmine.createSpy('MockPrimarySpec');
            MockProductStatus = jasmine.createSpy('MockProductStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Product': MockProduct,
                'ProductType': MockProductType,
                'Brand': MockBrand,
                'PrimarySpec': MockPrimarySpec,
                'ProductStatus': MockProductStatus
            };
            createController = function() {
                $injector.get('$controller')("ProductDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:productUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
