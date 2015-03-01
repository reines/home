import sys
from com.furnaghan.home.component.heating import HeatingType

if 'wfh' not in title.lower():
    print 'Ignored event: {}'.format(title)
    sys.exit(0)

print 'Working from home for {}'.format(duration)

thermostats = registry.getRegisteredComponents(HeatingType)
print 'Found {} heating systems'.format(len(thermostats))

for thermostat in thermostats:
    print 'Setting Study to 23 degrees'
    thermostat.setTemperature('study', 23)
