'use strict';

describe('Controller Tests', function() {

    describe('UserCategory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserCategory, MockCategory, MockUserBusinessRole;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserCategory = jasmine.createSpy('MockUserCategory');
            MockCategory = jasmine.createSpy('MockCategory');
            MockUserBusinessRole = jasmine.createSpy('MockUserBusinessRole');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserCategory': MockUserCategory,
                'Category': MockCategory,
                'UserBusinessRole': MockUserBusinessRole
            };
            createController = function() {
                $injector.get('$controller')("UserCategoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'traqtionApp:userCategoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
