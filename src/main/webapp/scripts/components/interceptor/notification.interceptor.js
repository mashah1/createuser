 'use strict';

angular.module('traqtionApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-traqtionApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-traqtionApp-params')});
                }
                return response;
            }
        };
    });
