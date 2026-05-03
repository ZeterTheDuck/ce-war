package com.cewar.game;

import com.cewar.library.Card;

/**
 * Represents the playing field
 * 
 * @deprecated use {@link GameData} instead
 */
public class Field {
    private GameCard[] field; // Representation of the field. Player 1 is stored at the "top", as in index 0.
    private GameCard[] sideField; // Representation of additional slots. Namely action slots and God card slot.
    private int width;
    private int height;
    private int actionSlots;

    private Player player1;
    private Player player2;

    /**
     * Creates an empty field.
     * 
     * @param width - width of the field, in spaces
     * @param height - height of the field, in spaces
     * @param player1
     * @param player2
     */
    public Field(int width, int height, Player player1, Player player2) {
        field = new GameCard[(width * height) + (2 * width) + 2];
        sideField = new GameCard[(2 * width) + 2];
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
        actionSlots = width + 1;
    }

    /**
     * Helper method to calculate the corresponding index, given a card's "coordinate."
     * 
     * @param width - Horizontal-coordinate of card
     * @param height - Vertical-coordinate of card
     * @return - corresponding index
     * @throws ArrayIndexOutOfBoundsException if the width or height are not valid field positions
     */
    private int getIndex(int width, int height) {
        if (width < 0 || width >= this.width || height < 0 || height >= this.height) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (height * this.height) + width;
    }

    /**
     * Converts a given field "coordinate" to the corresponding index in the field array.
     * (0,0) represents the bottom right corner from P1's perspective.
     * A 3x3 field, as in:
     *          P1 God
     *  (0,0)   (1,0)   (2,0)
     *  (0,1)   (1,1)   (2,1)
     *  (0,2)   (1,2)   (2,2)
     *          P2 God
     * Is represented by the array indices:
     *  0       1       2     
     *  3       4       5
     *  6       7       8
     * 
     * The "rows" -1 and -2 are reserved for referring to the additional slots for Player 1 and Player 2
     * respectively. For a 3x3 game:
     * (-1,0)           : P1 God
     * (-1,1) - (-1,3)  : P1 Actions
     * (-2,0)           : P2 God
     * (-2,1) - (-2, 3) : P2 Actions
     * 
     * @param width - Horizontal-coordinate of card, such that 0 <= card < field width
     * @param height - Vertical-coordinate of card, such that 0 <= card < field height
     * @return - Card at corresponding index
     * @throws ArrayIndexOutOfBoundsException if the width or height are not valid field positions
     */
    public Card get(int width, int height) {
        // return field[getIndex(width, height)];
        return null;
    }

    public int getActionSlotNum() {
        return this.actionSlots;
    }

    public void setActionSlotNum(int amount) {
        
    }

    // /**
    //  * Retrieves all cards on a given space, regardless of how they are attached.
    //  * 
    //  * @param width
    //  * @param height
    //  * @return
    //  */
    // public List<Card> getAll(int width, int height) {
    //     ArrayList<Card> output = new ArrayList<>();
    //     output.add(get(width, height));
    //     output.addAll(((GameCard) output.get(0)).getAttached());

    //     return output;
    // }


}
