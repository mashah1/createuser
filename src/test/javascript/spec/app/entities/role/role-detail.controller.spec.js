'use strict';

describe('Controller Tests', function() {

    describe('Role Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRole, MockRolePermission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRole = jasmine.createSpy('MockRole');
            MockRolePermission = jasmine.createSpy('MockRolePermission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Role': MockRole,
                'RolePermission': MockRolePermission
            };
            createController = function() {
                $injector.get('$controller')("RoleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:roleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
