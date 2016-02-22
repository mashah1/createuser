'use strict';

describe('Controller Tests', function() {

    describe('BusinessType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBusinessType, MockRolePermission, MockBusiness;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBusinessType = jasmine.createSpy('MockBusinessType');
            MockRolePermission = jasmine.createSpy('MockRolePermission');
            MockBusiness = jasmine.createSpy('MockBusiness');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BusinessType': MockBusinessType,
                'RolePermission': MockRolePermission,
                'Business': MockBusiness
            };
            createController = function() {
                $injector.get('$controller')("BusinessTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:businessTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
