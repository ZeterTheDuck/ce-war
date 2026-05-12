/**
 * @typedef {object} FilterObject
 * @property {String[]} archetypes
 * @property {String} type
 * @property {String} rarity
 * @property {String[]} attributes
 * @property {String} text - any additional text to filter by, by searching in the card description and other areas
 */

/**
 * Filters out cards from a collection of cards, based on a list of filters
 * 
 * @param {com.cewar.library.Card[]} cards List of cards
 * @param {FilterObject} filters - object containing filters to use
 * 
 * @returns {String[]} list of card ids that do not match the filters
 */
function filterCards(cards, filters) {
    var output = [];
    const textRegex = new RegExp("\\W", "g"); // Same as "a-zA-Z0-9_"


    // Test each card against filters
    for (let i = 0; i < cards.length; i++) {

        let removeCard = false;

        // Test Archetypes
        for (let j = 0; j < filters.archetypes.length; j++) {
            if (!cards[i].archetypes.includes(filters.archetypes[j])) {
                removeCard = true;
                break;
            }
        }

        // Test Type
        if (filters.type != "" && !(cards[i].type == filters.type)) {
            removeCard = true;
        }

        // Test Rarity
        if (filters.rarity != "" && !(cards[i].rarity == filters.rarity)) {
            removeCard = true;
        }

        // Test Attributes
        for (let j = 0; j < filters.attributes.length; j++) {
            if (!cards[i].attributes.includes(filters.attributes[j])) {
                removeCard = true;
                break;
            }
        }

        // Test Text
        // This gross formatting is needed because the `` String will count line breaks and indents
        let cardString =
            `${cards[i].name.replaceAll(textRegex, "")}
${cards[i].archetypes.toString().replaceAll(textRegex, "")}
${cards[i].materials.replaceAll(textRegex, "")}
${cards[i].effect.replaceAll(textRegex, "")}
${cards[i].flavorText.replaceAll(textRegex, "")}`.toLowerCase();

        if (!cardString.includes(filters.text.toLowerCase())) {
            removeCard = true;
        }

        // If any of the previous tests failed, take note of this card's ID so it can be hidden
        if (removeCard) {
            output.push(cards[i].id);
        }
    }

    return output;
}

/**
 * Gets the UCID (User-Card ID) from a jQuery object
 * 
 * @param {jQuery} cardObj - card to get the ID from
 */
function getUcid(cardObj) {

    const UCID_REGEX = new RegExp("\\b(?=\\w)ucid-(-?\\d+)");

    try {
        return cardObj.attr("class").match(UCID_REGEX)[1];
    } catch {

    }

}