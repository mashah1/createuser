'use strict';

angular.module('traqtionApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


