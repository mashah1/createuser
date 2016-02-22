'use strict';

describe('Controller Tests', function() {

    describe('SupplierSite Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSupplierSite, MockSpecification;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSupplierSite = jasmine.createSpy('MockSupplierSite');
            MockSpecification = jasmine.createSpy('MockSpecification');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SupplierSite': MockSupplierSite,
                'Specification': MockSpecification
            };
            createController = function() {
                $injector.get('$controller')("SupplierSiteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:supplierSiteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
