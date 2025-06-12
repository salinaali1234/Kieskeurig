# VoteController API

### Method: GET

### Overview
Retrieve the total votes per party from the XML file 
(e.g., Totaaltelling_TK2023.eml.xml), 
and you can sort by vote count or party name.
### URL
```
/api/xml/votes/parties
```


### Query Parameters

year (optional, default: 2023) – The election year to load results from.

sort (optional, default: none) – Sorting options:

votes-desc

votes-asc

name-asc

name-desc

### Response
status(200): Returns a map with party names as keys and vote counts as values.

status(400): Returned if an invalid sort option or year is provided.

status(500): Returned if loading or parsing the vote data fails.
    

