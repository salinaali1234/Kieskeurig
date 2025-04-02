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
/api/candidates/{electionId}/{partyName}
```

###### Parameters
- `electionId` - The election from which you want the data **(Required)**.
- `partyName` - The name of the party you want the candidates from **(Required)**.

###### Response
`status(200)` and a list containing all candidates saved in the repository from the specified party in the specified election.
Candidates use the `Candidate` class.
