'use strict';

describe('Controller Tests', function() {

    describe('Broadcast Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBroadcast;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBroadcast = jasmine.createSpy('MockBroadcast');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Broadcast': MockBroadcast
            };
            createController = function() {
                $injector.get('$controller')("BroadcastDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'natureseyesApp:broadcastUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
