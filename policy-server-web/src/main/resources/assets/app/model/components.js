var NamedType = function(name, type) {
    return {
        name: name,
        type: type
    };
};

var Component = Backbone.Model.extend({
    defaults: {
        title: 'Unknown',
        type: {},
        config_type: {},
        inherited_types: [],
        events: [],
        actions: []
    },
    parse: function(response) {
        var key = _.first(response);
        var value = _.last(response);
        return {
            title: key,
            type: NamedType(value.name.name, value.name.type),
            config_type: NamedType(value.configuration.name, value.configuration.type),
            inherited_types: _.map(value.types, function(type) {
                return NamedType(type.name, type.type);
            }),
            events: value.events,
            actions: value.actions
        };
    }
});

var Components = Backbone.Collection.extend({
    model: Component,
    url: '/components',
    parse: function(response) {
        return _.pairs(response);
    }
});

var ComponentType = Backbone.Model.extend({
    defaults: {
        name: {},
        config_type: {},
        inherited_types: [],
        events: [],
        actions: []
    },
    parse: function(value) {
        return {
            name: NamedType(value.name.name, value.name.type),
            config_type: NamedType(value.configuration.name, value.configuration.type),
            inherited_types: _.map(value.types, function(type) {
                return NamedType(type.name, type.type);
            }),
            events: value.events,
            actions: value.actions
        };
    }
});

var ComponentTypes = Backbone.Collection.extend({
    model: ComponentType,
    url: '/components/types'
});
