'use strict';

angular.module('traqtionApp')
    .factory('ManufacturerSearch', function ($resource) {
        return $resource('api/_search/manufacturers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
