'use strict';

angular.module('traqtionApp')
    .factory('SupplierSite', function ($resource, DateUtils) {
        return $resource('api/supplierSites/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
