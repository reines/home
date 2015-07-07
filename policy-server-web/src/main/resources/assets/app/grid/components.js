var ComponentTypesGrid = Backgrid.Grid.extend({
    columns: [{
        name: 'name',
        label: 'Type',
        cell: NamedTypeCell,
        editable: false
    }, {
        name: 'inherited_types',
        label: 'Inherited Types',
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
        cell: NamedTypeCell,
        editable: false
    }]
});

var ComponentsGrid = Backgrid.Grid.extend({
    columns: [{
        name: 'title',
        label: 'Title',
        cell: HTMLCell,
        editable: false,
        formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
            fromRaw: function(rawValue, model) {
                return $("<a>", {
                    tabIndex: -1,
                    href: '/components/' + model.get('type').type + '/' + model.get('title') + '/config.json'
                }).text(rawValue)[0].outerHTML;
            }
        })
    }, {
        name: 'type',
        label: 'Type',
        cell: NamedTypeCell,
        editable: false
    }]
});
