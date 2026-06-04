/**
 * Formats the first blank card div to display related info
 * 
 * @param {*} card object containing card data, passed through Thymeleaf
 */
function formatCardFirst(card) {
  // Pick the first blank card
  var cardDiv = $(".card-table.blank-card").first();
  formatCardDiv(card, cardDiv);
}

/**
 * Formats a specific card div to display related info
 * 
 * @param {*} card object containing card data, passed through Thymeleaf
 * @param {*} cardDiv div object to modify
 */
function formatCardDiv(card, cardDiv) {

  // Prevent this card from being selected again by removing all classes
  // Just removing ".blank-card" would be sufficient, but the selected card can get multiple card IDs
  cardDiv.removeClass();

  // Add back "card-table" class
  cardDiv.addClass("card-table");

  // Add card id to class of the card table for filtering
  cardDiv.addClass(card.id);

  // Construct card name field, adding any needed attribute symbols
  let cardAttributes = [];
  for (let i = 0; i < card.attributes.length; i++) {
    cardAttributes[i] = formatSymbol(card.attributes[i]);
  }
  cardDiv.find(".name").html(cardAttributes.join("") + " " + card.name);

  // Determine card rarity
  switch (card.rarity) {
    case "COMMON":
      cardDiv.find(".rarity").attr("src", "/images/rarity_common.png");
      break;
    case "UNCOMMON":
      cardDiv.find(".rarity").attr("src", "/images/rarity_uncommon.png");
      break;
    case "RARE":
      cardDiv.find(".rarity").attr("src", "/images/rarity_rare.png");
      break;
    case "ULTRA_RARE":
      cardDiv.find(".rarity").attr("src", "/images/rarity_ultra_rare.png");
      break;
    case "NONE":
      if (card.type == "GOD") {
        // Stretch card text div to take up entire width of card.
        cardDiv.find(".name").css("width", "calc(125% - 10px)"); // Accomodate border space
        cardDiv.find(".rarity").style = "display: none;" // Hide rarity symbol
      }
      break;
    default:
  }

  // Add Card Art
  cardDiv.find(".art").attr("src", card.artSource);

  // Construct card description
  cardDiv.find(".desc").html(constructCardDesc(card));

  // Determine left footer text
  if (card.attack == -1) {
    if (card.size.length == 2) {
      cardDiv.find(".attack").text(card.size[0] + "x" + card.size[1]);
    } else {
      cardDiv.find(".attack").text("");
    }
  } else if (card.attack == -2) {
    cardDiv.find(".attack").text("???");
  } else {
    cardDiv.find(".attack").text(card.attack);
  }

  // Determine right footer text
  if (card.health == -2) {
    cardDiv.find(".health").text("???");
  } else {
    if (card.health == -1) {
      cardDiv.find(".health").text("");
    } else {
      cardDiv.find(".health").text(card.health);
    }
    if (card.god) {
      cardDiv.find(".health").append(formatSymbol("back-slot")); // Could also do "[]" instead
    }
  }

  // Determine card color
  switch (card.type) {
    case "CREATURE":
      var darkColor = "#7fdc39";
      var lightColor = "#b6d7a8";
      break;
    case "ACTION":
      var darkColor = "#4c4cff";
      var lightColor = "#9fc5e8";
      break;
    case "BUILDING":
      var darkColor = "#ff0000";
      var lightColor = "#ea9999";
      break;
    case "MATERIAL":
      var darkColor = "#ffff00";
      var lightColor = "#ffe599";
      break;
    case "GOD":
      var darkColor = "#ff9900";
      var lightColor = "#f9cb9c";
      break;
    case "REPLICA":
      var darkColor = "#b7b7b7";
      var lightColor = "#d9d9d9";
      break;
    default:
      var darkColor = "#ffffff";
      var lightColor = "#ffffff";
  }
  cardDiv.find(".dark-color").css("background-color", darkColor);
  cardDiv.find(".light-color").css("background-color", lightColor);

  // Display shiny div if applicable
  if (card.shiny) {
    cardDiv.find(".card-shine").css("visibility", "visible");
  }

  // Adjust text scaling (at least trying to)
  // TODO do this in a better way. This is not pretty and has lots of problems with it
  if ((card.effect.length + card.flavorText.length) > 150) {
    cardDiv.find(".desc").css("font-size", "0.8em");
  }

  // SECTION Script Functions

  /** Constructs a card symbol in the form of an html span element.
   * This element contains a svg with size 1em of the given symbolType, and the same color as the card's type.
   * This element has the "card-symbol" class.
   * 
   * @param symbolType - which symbol to create. Valid symbols are c1, c2, c3, c4, stage, equip, god, back-slot, [] 
   */
  function formatSymbol(symbolType) {
    // Determine which symbol to use
    switch (symbolType.toLowerCase()) {
      case "c1": case "c2": case "c3": case "c4":
      case "stage": case "equip": case "god":
        // symbol type is valid
        var symbolID = symbolType.toLowerCase();
        break;
      case "back-slot": case "[]":
        // symbol type is valid
        var symbolID = "back-slot";
        break;
      default:
        // Not a valid symbol, might just be something in parentheses. Return the text as it should be.
        return symbolType;
    }

    // Valid types: CREATURE, ACTION, BUILDING, MATERIAL, GOD, REPLICA, OTHER
    switch (card.type) {
      case "CREATURE": case "ACTION": case "BUILDING": case "MATERIAL": case "GOD": case "REPLICA":
        var symbolColor = `var(--${card.type.toLowerCase()}-color)`;
        break;
      case "OTHER":
        if (card.id == "gap") {
          var symbolColor = "var(--gap-color)";
          break;
        }
      // else: Continue to default case
      default:
        var symbolColor = "#000000";
        // Log error to website console. Symbol color should be black.
        console.error("Invalid card type of type \"" + card.type + "\"");
    }

    // Combine strings into final html element
    return `<svg class="card-symbol" role="img" aria-labelledby="title">
  <title id="title">${symbolID}</title>
  <use href="/images/card_symbol.svg#${symbolID}" style="--icon-color: ${symbolColor};"/>
</svg>`;
  }

  // Formats card effects to include line breaks, italics, and lists.
  function formatEffect(string) {
    const listRegex = new RegExp("(^[0-9]+: )|(^- )");

    // Split effect, based on line breaks for further processing
    let cardEffectArr = string.split("\n");

    for (let i = 0; i < cardEffectArr.length; i++) {
      if (listRegex.test(cardEffectArr[i])) {
        // If this is a list item

        if (i == 0 | cardEffectArr[i - 1].charAt(0) != "<") {
          // If previous item was not a list item
          if (cardEffectArr[i].charAt(0) == "-") {
            var isOrdered = false;
            cardEffectArr[i] = cardEffectArr[i].replace(listRegex, "<li>") + "</li>";
            cardEffectArr[i] = "<ul>" + cardEffectArr[i];
          } else {
            var isOrdered = true;
            cardEffectArr[i] = cardEffectArr[i].replace(listRegex, "<li>") + "</li>";
            cardEffectArr[i] = "<ol>" + cardEffectArr[i];
          }
        } else {
          // Ensure this is a list item
          // This allows the regex to work properly and avoid issues
          cardEffectArr[i] = cardEffectArr[i].replace(listRegex, "<li>") + "</li>";
        }

        if (cardEffectArr[i].charAt(0) == "<" && i == cardEffectArr.length - 1) {
          // If this is the last item in the list
          if (isOrdered) {
            cardEffectArr[i] = cardEffectArr[i] + "</ol>";
          } else {
            cardEffectArr[i] = cardEffectArr[i] + "</ul>";
          }
        }
      } else {
        // Not a list item
        if (i > 0 && listRegex.test(cardEffectArr[i - 1])) {
          // If previous index was a list item
          if (isOrdered) {
            cardEffectArr[i - 1] = cardEffectArr[i - 1] + "</ol>";
          } else {
            cardEffectArr[i - 1] = cardEffectArr[i - 1] + "</ul>";
          }
        }
        cardEffectArr[i] = cardEffectArr[i] + "<br>";
      }
    }

    let cardEffect = cardEffectArr.join("");
    const symbolRegex = new RegExp("\\(\\S+\\)", "g");
    cardEffect = cardEffect.replaceAll(symbolRegex, function (match) {
      return formatSymbol(match.slice(1, -1)); // Remove outer parentheses
    });

    return cardEffect;
  }

  // Constructs card description field from multiple data sources.
  function constructCardDesc() {
    let cardArchetypesString = "";
    let cardArchetypes = [];
    if (card.archetypes.length > 0) {
      // Format archetypes to be lowercase, since the enums are declared as uppercase
      for (let i = 0; i < card.archetypes.length; i++) {
        cardArchetypes[i] = card.archetypes[i].charAt(0) + card.archetypes[i].slice(1).toLowerCase();
      }
      cardArchetypesString = "<em>" + cardArchetypes.join(", ") + "</em><br>";
    }
    let cardMaterials = "";
    if (card.materials.length > 0) {
      cardMaterials = "<em>" + card.materials + "</em><br>";
    }
    let cardFlavorText = "";
    if (card.flavorText.length > 0) {
      if (card.effect.length > 0) {
        cardFlavorText = "<br>";
      }
      cardFlavorText = cardFlavorText + "<em>" + card.flavorText + "</em>";
    }

    let cardEffect = formatEffect(card.effect);

    return cardArchetypesString + cardMaterials + cardEffect + cardFlavorText;
  }
  // !SECTION
}