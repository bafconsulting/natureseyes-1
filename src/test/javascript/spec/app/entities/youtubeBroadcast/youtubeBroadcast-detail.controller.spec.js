'use strict';

describe('Controller Tests', function() {

    describe('YoutubeBroadcast Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockYoutubeBroadcast;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockYoutubeBroadcast = jasmine.createSpy('MockYoutubeBroadcast');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'YoutubeBroadcast': MockYoutubeBroadcast
            };
            createController = function() {
                $injector.get('$controller')("YoutubeBroadcastDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'natureseyesApp:youtubeBroadcastUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
