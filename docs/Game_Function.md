# Game Function
This is an outline of how the API Endpoints for the game should work.

## Client to Backend Actions

### API Actions
Most actions are sent to the backend by POST requests to the `/api/v1/games/` endpoint. Most use the `game-id` parameter, which should be the game's ID. All requests should be authenticated.

When a client makes a request to the API, the backend should add it to a queue of tasks. When a task is done, an "OK" response will be given. When the backend completes the last tasks for a game, it will send out a websocket response with an updated game board.

There are numerous endpoints:

- `GET api/v1/games/create`: creates a new game, returns a game ID which the user connects to via websocket.
  - Optional: board width and height

- `POST api/v1/games/{game-id}/card`: sets values for a card. This will probably be the most used endpoint.
  - (required) User-Card ID
  - location
  - X position
  - Y position
  - attack
  - health
  - max health
  - attachedTo (use UC-ID)
  - attachedCards (mapped by UC-ID)
  - rotated
  - visible
  - something to do with counters
    - Counter name
    - New value only (frontend calculates this)
  - size (may just be making a card be able to take up more than one X or Y position?)
  - owner

- `POST api/v1/games/{game-id}/draw`: draws a card
  - (required) is Player one
  - draw slot (0, 1, 2)

- `POST api/v1/games/{game-id}/add`: add a card to the game. For replicas, worry about this at a later point