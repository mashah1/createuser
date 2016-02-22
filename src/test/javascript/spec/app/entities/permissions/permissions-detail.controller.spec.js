'use strict';

describe('Controller Tests', function() {

    describe('Permissions Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPermissions, MockRolePermission;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPermissions = jasmine.createSpy('MockPermissions');
            MockRolePermission = jasmine.createSpy('MockRolePermission');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Permissions': MockPermissions,
                'RolePermission': MockRolePermission
            };
            createController = function() {
                $injector.get('$controller')("PermissionsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:permissionsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
