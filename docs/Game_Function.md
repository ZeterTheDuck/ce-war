# Game Function
This is an outline of how the API Endpoints for the game should work.

## Client to Backend Actions

### Websocket Actions
Most actions are sent to the backend through a [GameMessageDto] that has fields for several different types of actions. Each one should have a game ID and a user ID, as well as any number of the following types of actions. Perhaps a list of objects?

- Set Card Values
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
- Draw card
  - optional parameter for draw slot
- Add card
  - used for replicas... worry about this later

### Other Actions
Other actions are mainly HTTP requests for making and ending games. Ending games may be done through a websocket.

- `POST /api/games/create`
  - User 1 ID
  - User 2 ID
  - User 1 Deck ID
  - User 2 Deck ID
  - *honestly no clue how you're meant to get the other user's ID*

[GameMessageDto]: ../src/main/java/com/cewar/model/dtos/GameMessageDto.java