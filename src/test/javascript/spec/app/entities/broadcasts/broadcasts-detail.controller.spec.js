'use strict';

describe('Controller Tests', function() {

    describe('Broadcasts Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBroadcasts;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBroadcasts = jasmine.createSpy('MockBroadcasts');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Broadcasts': MockBroadcasts
            };
            createController = function() {
                $injector.get('$controller')("BroadcastsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'natureseyesApp:broadcastsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
