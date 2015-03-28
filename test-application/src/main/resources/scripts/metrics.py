from com.furnaghan.home.component.metrics import MetricsType

# Find all components of MetricsType, and send them this value
for component in registry.getByType(MetricsType):
    component.send(name, value)
