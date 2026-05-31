package com.cewar.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.cewar.model.session.GameData;
import com.cewar.util.GameIdGenerator;

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
    private HashMap<String, GameData> games;

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
    public GameData get(String gameId) {
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
        games.put(newGame.getId(), newGame);
    }

    public String createGame(int width, int height) {
        String newId = GameIdGenerator.generateId();
        // In the case of a collision, generate a new ID
        while(games.containsKey(newId)) {
            newId = GameIdGenerator.generateId();
        }

        // Create and map new game
        games.put(newId, new GameData(newId, width, height));
        return newId;
    }
}
