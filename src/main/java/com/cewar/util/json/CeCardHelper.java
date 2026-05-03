package com.cewar.util.json;

import java.util.Scanner;

/**
 * @deprecated
 * Tool to manually add cards. You should parse json instead.
 */
public class CeCardHelper {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringBuilder jBuilder = new StringBuilder();
        jBuilder.append(",");
        while (true) {

            // Card Name
            System.out.print("Card name (use \"!end\" to end): ");
            String cardName = input.nextLine();
            if (cardName.equals("!end")) {
                break;
            }
            jBuilder.append("\n{\n\t\"name\" : \"" + cardName + "\",");

            // Card Rarity
            System.out.print("Card rarity: ");
            String cardRarity = input.nextLine().toUpperCase();
            jBuilder.append("\n\t\"rarity\" : \"" + cardRarity + "\",");

            // Card Type
            System.out.print("Card type: ");
            String cardType = input.nextLine().toUpperCase();
            jBuilder.append("\n\t\"type\" : \"" + cardType + "\",");

            // Card Attributes
            System.out.print("Card attributes, separated by commas: ");
            String[] atrStringArr = input.nextLine().toUpperCase().split("[,\\s]");
            jBuilder.append("\n\t\"attribute\" : [");
            for (int i = 0; i < atrStringArr.length - 1; i++) {
                if (!atrStringArr[i].equals("")) {
                    jBuilder.append("\"" + atrStringArr[i] + "\", ");
                }
            }
            if (!atrStringArr[0].equals("")) {
                jBuilder.append("\"" + atrStringArr[atrStringArr.length - 1] + "\"");
            }
            jBuilder.append("],");

            // Card Archetypes
            System.out.print("Card archetypes, separated by commas: ");
            String[] archStringArr = input.nextLine().toUpperCase().split("[,\\s]");
            jBuilder.append("\n\t\"archetype\" : [");
            for (int i = 0; i < archStringArr.length - 1; i++) {
                if (!archStringArr[i].equals("")) {
                    jBuilder.append("\"" + archStringArr[i] + "\", ");
                }
            }
            if (!archStringArr[0].equals("")) {
                jBuilder.append("\"" + archStringArr[archStringArr.length - 1] + "\"");
            }
            jBuilder.append("],");

            // Card attack
            System.out.print("Card attack: ");
            jBuilder.append("\n\t\"attack\" : " + input.nextLine() + ",");

            // Card size
            System.out.print("Card size, separated by a comma: ");
            String[] sizeStringArr = input.nextLine().toUpperCase().split("[,\\s]");
            jBuilder.append("\n\t\"size\" : [");
            if (sizeStringArr[0].equals("")) {
                jBuilder.append("],");
            } else if (sizeStringArr.length == 2) {
                jBuilder.append(sizeStringArr[0] + ", " + sizeStringArr[1] + "],");
            } else {
                System.out.println("Invalid size! Using the default instead.");
                jBuilder.append("],");
            }

            // Card health
            System.out.print("Card health: ");
            jBuilder.append("\n\t\"health\" : " + input.nextLine() + ",");

            // Is card a god card?
            System.out.print("Is this a God Card? y/n: ");
            String isGodString = input.nextLine();
            switch (isGodString) {
                case ("y"):
                    jBuilder.append("\n\t\"isGod\" : true,");
                    break;
                default:
                    jBuilder.append("\n\t\"isGod\" : false,");
            }

            // Card Materials
            System.out.print("Card materials, separated by commas: ");
            String[] matStringArr = input.nextLine().split("[,\\s]");
            jBuilder.append("\n\t\"materials\" : [");
            for (int i = 0; i < matStringArr.length - 1; i++) {
                if (!matStringArr[i].equals("")) {
                    jBuilder.append("\"" + matStringArr[i] + "\", ");
                }
            }
            if (!archStringArr[0].equals("")) {
                jBuilder.append("\"" + matStringArr[matStringArr.length - 1] + "\"");
            }
            jBuilder.append("],");

            // Card effect
            System.out.print("Card Effect: ");
            jBuilder.append("\n\t\"effect\" : " + input.nextLine() + "\",");

            // Flavor Text
            System.out.print("Flavor Text: ");
            jBuilder.append("\n\t\"flavorText\" : " + input.nextLine() + "\",");

            System.out.println();
        }

        input.close();
        System.out.println();
        System.out.println(jBuilder.toString());
    }
}
