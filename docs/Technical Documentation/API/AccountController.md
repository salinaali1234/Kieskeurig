# Uitleg AccountController en AuthenticationController

# AccountsController API

## /accounts

### GET

###### Overview
Retrieve a list of all user accounts stored in the system.

###### URL
###### Response
- `status(200)` and a JSON array containing all `Account` objects.

---

## /accounts/{id}

### GET

###### Overview
Retrieve a specific user account by its unique ID.

###### URL
###### Parameters
- `id` - The unique identifier of the account to fetch **(Required)**.

###### Response
- `status(200)` and the `Account` object with the specified ID.
- `status(404)` if no account is found with that ID.

---



