$(document).ready(function() {
    var componentTypes = new ComponentTypes();
    componentTypes.fetch().then(function() {
        console.log('Loaded ' + componentTypes.models.length + ' component types.');
        var componentTypesGrid = new ComponentTypesGrid({
            collection: componentTypes
        });
        $('#component_types').empty().append(componentTypesGrid.render().el);
    });

    var components = new Components();
    components.fetch().then(function() {
        console.log('Loaded ' + components.models.length + ' components.');
        var componentsGrid = new ComponentsGrid({
            collection: components
        });
        $('#components').empty().append(componentsGrid.render().el);
    });

    var policies = new Policies();
    policies.fetch().then(function() {
        console.log('Loaded ' + policies.models.length + ' policies.');
        var policiesGrid = new PoliciesGrid({
            collection: policies
        });
        $('#policies').empty().append(policiesGrid.render().el);
    });
});
