# CandidateController API



## /party/{electionId}/{province}

### GET

###### Overview
Get get the total amount of votes of each party for the specified election and province.

###### URL
```
/api/party/{electionId}/{province}
```

###### Parameters
- `electionId` - The election from which you want the data **(Required)**.
- `province` - The province from which you want the data **(Required)**.

###### Response
`status(200)` and a map containing the total amount of votes for each party in the specified province in the specified election.
