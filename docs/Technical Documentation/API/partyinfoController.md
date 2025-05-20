###### URL
```
/api/partiesInfo
```
---

## GET /api/partiesInfo

###  Overzicht
Haalt alle partijen met verkiezingsdata op.

### Response
- 200 OK: Lijst van ElectionForParty objecten.
- 500 Internal Server Error: Bij lees- of parsefouten.

---

## GET /api/partiesInfo/parties

### Overzicht
Haalt een lijst van alle partijen inclusief hun zetelaantallen, met optionele sortering.

### Query Parameters
| Parameter | Beschrijving                      | Opties                       | Default    |
|---------|-----------------------------------|-------------------------------|------------|
| sort    | Sorteervolgorde van partijen      | seats-desc, seats-asc, alphabetical | seats-desc |

###  Response
- 200 OK: Lijst van PartyWithInfo objecten.
- 500 Internal Server Error: Bij fout tijdens lezen/parsen van de bestanden.

---

## GET /api/partiesInfo/parties/{partyId}

###  Overzicht
Geeft informatie over één specifieke partij.

###  Path Parameters
| Parameter | Beschrijving              |
|----------|---------------------------|
| partyId  | ID van de gewenste partij |

###  Response
- 200 OK: Eén PartyWithInfo in een lijst.
- 404 Not Found: Geen partij gevonden.
- 500 Internal Server Error: Bij I/O of XML-fout.

---

## GET /api/partiesInfo/candidates/{partyId}

###  Overzicht
Haalt alle kandidaten op die horen bij een specifieke partij.

###  Path Parameters
| Parameter | Beschrijving              |
|----------|---------------------------|
| partyId  | ID van de gewenste partij |

###  Response
- 200 OK: Lijst van CandidateForPartyInfo objecten.
- 500 Internal Server Error: Bij fout tijdens ophalen.

---

## GET /api/partiesInfo/candidates/elected

###  Overzicht
Haalt **alle verkozen kandidaten** op (gefilterd uit alle kandidaten).

###  Response
- 200 OK: Lijst van CandidateForPartyInfo waar isElected = true.
- 500 Internal Server Error: Bij fout tijdens ophalen.

---

## Opmerking

De controller vangt intern fouten zoals IOException of XMLStreamException af. Bij een fout retourneert het een lege lijst of een statuscode 500.

---