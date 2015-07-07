var PoliciesGrid = Backgrid.Grid.extend({
    columns: [{
        name: 'script',
        label: 'Script',
        cell: HTMLCell,
        editable: false,
        formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
            fromRaw: function(rawValue, model) {
                return $("<a>", {
                    tabIndex: -1,
                    href: 'http://localhost:7072/scripts/' + rawValue
                }).text(rawValue)[0].outerHTML;
            }
        })
    }, {
        name: 'event',
        label: 'Event',
        cell: 'string',
        editable: false,
        formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
            fromRaw: function(rawValue, model) {
                return rawValue.type.type + '#' + rawValue.method;
            }
        })
    }]
});
