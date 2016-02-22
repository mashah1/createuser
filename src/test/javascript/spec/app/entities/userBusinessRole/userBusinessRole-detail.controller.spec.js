'use strict';

describe('Controller Tests', function() {

    describe('UserBusinessRole Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserBusinessRole, MockUser, MockRole, MockBusiness;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserBusinessRole = jasmine.createSpy('MockUserBusinessRole');
            MockUser = jasmine.createSpy('MockUser');
            MockRole = jasmine.createSpy('MockRole');
            MockBusiness = jasmine.createSpy('MockBusiness');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserBusinessRole': MockUserBusinessRole,
                'User': MockUser,
                'Role': MockRole,
                'Business': MockBusiness
            };
            createController = function() {
                $injector.get('$controller')("UserBusinessRoleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:userBusinessRoleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
