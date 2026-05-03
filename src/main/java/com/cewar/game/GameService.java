package com.cewar.game;

import java.util.HashMap;

import org.springframework.stereotype.Service;

/**
 * A service to manage game objects
 * 
 * <p> Essentially functions as a HashMap for Integer keys and GameData values, but is a Spring Component.
 */
@Service
public class GameService {
    
    /**
     * Games accessed through this service. The key for this map is the game IDs
     */
    private HashMap<Integer, GameData> games;

    /**
     * Default constructor.
     */
    public GameService() {

    }

    /**
     * Gets the gameData associated with an ID
     * 
     * @param gameId - the ID of the gameData object to retrieve
     * @return - gameData object associated with the gameId, or null if there is none.
     */
    public GameData get(int gameId) {
        return games.get(gameId);
    }

    /**
     * Sets or updates gameData in this service. 
     * 
     * <p> If a GameData object with the same ID is already present, this will overwrite it.
     * 
     * @param newGame - gameData object to add or replace 
     */
    public void put(GameData newGame) {
        games.put(newGame.getGameId(), newGame);
    }
}
