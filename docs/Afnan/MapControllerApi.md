# MapController API

### Method: GET

### Overview
Returns a GeoJSON file containing 
geographic data for Dutch provinces.
### URL
```
/api/map/map
```

### Response
status(200): Returns the contents of provinces.geojson as a JSON string.

status(500): Returned if the file cannot be loaded from the classpath.