var HTMLCell = Backgrid.Cell.extend({
    render: function () {
        this.$el.empty();
        var model = this.model;
        this.$el.html(this.formatter.fromRaw(model.get(this.column.get("name")), model));
        this.delegateEvents();
        return this;
    }
});

var NamedTypeCell = HTMLCell.extend({
    formatter: _.extend({}, Backgrid.CellFormatter.prototype, {
        fromRaw: function(rawValue, model) {
            return $("<span>", {
                title: rawValue.type
            }).text(rawValue.name)[0].outerHTML;
        }
    })
});
