var ComponentTypesGrid = Backgrid.Grid.extend({
    columns: [{
        name: 'name',
        label: 'Component',
        cell: NamedTypeCell,
        editable: false
    }, {
        name: 'inherited_types',
        label: 'Types',
        cell: 'string',
        formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
            fromRaw: function(rawValue, model) {
                return _.map(rawValue, function(type) {
                    return type.name;
                }).join(', ');
            }
        }),
        editable: false
    }, {
        name: 'config_type',
        label: 'Configuration',
        cell: HTMLCell,
        editable: false,
        formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
            fromRaw: function(rawValue, model) {
                return $('<a>', {
                    href: '/components/' + model.get('name').type + '/config_schema.json'
                }).html(NamedTypeFormatter.fromRaw(rawValue, model))[0].outerHTML;
            }
        })
    }, {
        name: 'events',
        label: 'Events',
        cell: StringArrayCell,
        editable: false
    }, {
        name: 'actions',
        label: 'Actions',
        cell: StringArrayCell,
        editable: false
    }]
});

var ComponentsGrid = Backgrid.Grid.extend({
    columns: [{
        name: 'title',
        label: 'Title',
        cell: 'string',
        editable: false
    }, {
        name: 'type',
        label: 'Component',
        cell: NamedTypeCell,
        editable: false
    }, {
        name: 'config_type',
        label: 'Configuration',
        cell: HTMLCell,
        editable: false,
        formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
            fromRaw: function(rawValue, model) {
                return $('<a>', {
                    href: '/components/' + model.get('type').type + '/' + model.get('title') + '/config.json'
                }).html(NamedTypeFormatter.fromRaw(rawValue, model))[0].outerHTML;
            }
        })
    }]
});
