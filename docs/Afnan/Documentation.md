# VoteController API

### Method: GET

### Overview
Retrieve the total votes per party from the xml file ```Totaaltelling_TK2023.eml.xml```.

### URL
```
/api/xml/votes/parties
```

###### Response
status(200): Returns a map containing the names of the parties as keys and the corresponding number of valid votes as values.

The data is returned in the form of a Map<String, Integer>.
    

    