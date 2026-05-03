package com.cewar.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Takes a directory of cards and crops them to only contain the card art
 */
public class CardCropper {

    static File rootDir;
    static File[] seriesDir;
    static String outputUrl;

    // Regex patterns to parse out card name.
    static final String REGEX_1 = ".*\\\\Cards\\\\[a-zA-Z]+\\\\";
    static final String REGEX_2 = "\\.png";

    /**
     * Crops all cards to only their card art, using predetermined values.
     * This should be used on a downloaded folder from the Google Drive.
     * 
     * @param srcUrl
     *      "C:\Users\zeter\OneDrive\CE War\Cards"
     * @param destUrl
     *      "C:\Users\zeter\OneDrive\CE War\Output"
     * @throws IOException 
     */
    public static void cropCards(String srcUrl, String destUrl) throws IOException {
        rootDir = new File(srcUrl);
        seriesDir = rootDir.listFiles();
        outputUrl = destUrl;

        for (int i = 0; i < seriesDir.length; i++) {
            if (seriesDir[i].toPath().toString().equals("C:\\Users\\zeter\\OneDrive\\CE War\\Cards\\Extras")) {
                continue;
            }
            File[] seriesCards = seriesDir[i].listFiles();
            for (int j = 0; j < seriesCards.length; j++) {
                String fileName = seriesCards[j].toPath().toString().replaceAll(REGEX_1, "").replaceAll(REGEX_2, "");
                fileName = NameFormatter.formatName(fileName);
                savePNG(cropImage(ImageIO.read(seriesCards[j])), fileName);
            }
        }
    }

    /**
     * Copys all cards from a directory to another, checking all folders 1 directory deep.
     * This should be used on a downloaded folder from the Google Drive.
     * 
     * @param srcUrl
     * @param destUrl
     * @throws IOException
     */
    public static void copyCards(String srcUrl, String destUrl) throws IOException {
        rootDir = new File(srcUrl);
        seriesDir = rootDir.listFiles();
        outputUrl = destUrl;

        for (int i = 0; i < seriesDir.length; i++) {
            if (seriesDir[i].toPath().toString().equals("C:\\Users\\zeter\\OneDrive\\CE War\\Cards\\Extras")) {
                continue;
            }
            File[] seriesCards = seriesDir[i].listFiles();
            for (int j = 0; j < seriesCards.length; j++) {
                String fileName = seriesCards[j].toPath().toString().replaceAll(REGEX_1, "").replaceAll(REGEX_2, "");
                fileName = NameFormatter.formatName(fileName);
                savePNG(ImageIO.read(seriesCards[j]), fileName);
            }
        }
    }


    /**
     * Helper method to actually crop the cards themselves.
     * 
     * Card Selection, from top left:
     * (11,77) (exclusive) to (409,264) (inclusive)
     * 
     * @param src - Source image
     * @return - Cropped Image
     */
    private static BufferedImage cropImage(BufferedImage src) {
        BufferedImage output = src.getSubimage(11, 77, 410 - 11, 265 - 77);
        return output;
    }

    /**
     * Saves a card to the computer
     * 
     * @param src - source image
     * @param cardName - name of card to write as
     * @throws IOException - if File could not be created (outputUrl is invalid)
     */
    private static void savePNG(BufferedImage src, String cardName) throws IOException {
        ImageIO.write(src, "png", new File(outputUrl + "/" + cardName + ".png"));
    }
}
