# CandidateController API



## /candidates

### GET

###### Overview
Get all candidates saved in the repository.

###### URL
```
/api/candidates
```

###### Response
`status(200)` and a list containing all candidates saved in the repository.
Candidates use the `Candidate` class.



## /candidates/{partyId}/{year}

### GET

###### Overview
Get all candidates saved in the repository from a specified party in a specified year.

###### URL
```
/api/candidates/{partyId}/{year}
```

###### Parameters
- `partyId` - The ID of the party you want the candidates from **(Required)**.
- `year` - The year you want the data from **(Required)**.

###### Response
`status(200)` and a list containing all candidates saved in the repository from the specified party in the specified year.
Candidates use the `Candidate` class.
