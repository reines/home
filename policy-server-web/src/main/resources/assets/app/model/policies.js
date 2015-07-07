var Policy = Backbone.Model.extend({
    defaults: {
        event: {
            type: {},
            method: null,
            params: null
        },
        script: null
    },
    parse: function(response) {
        return {
            event: {
                type: NamedType(response.type.name, response.type.type),
                method: response.event,
                params: _.map(response.parameterTypes, function(type) {
                    return type.type;
                })
            },
            script: response.script
        };
    }
});

var Policies = Backbone.Collection.extend({
    model: Policy,
    url: '/policies'
});
