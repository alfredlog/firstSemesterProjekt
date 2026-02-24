package hProjekt.model.cards;

import java.util.function.BiFunction;

import hProjekt.model.Player;
import hProjekt.model.grid.Types;

/**
 * Represents the types of path cards available in the game.
 * Each type has a function to construct the corresponding card.
 */
public enum CardType {
    IN_AREA(InAreaCard::new),
    IN_BIGGEST_AREA(InBiggestAreaCard::new),
    NEXT_TO(NextToCard::new),
    CAN_SEE(CanSeeCard::new),
    NOT_IN_AREA(IN_AREA),
    NOT_IN_BIGGEST_AREA(IN_BIGGEST_AREA),
    NOT_NEXT_TO(NEXT_TO),
    NOT_CAN_SEE(CAN_SEE);

    public final BiFunction<Player, Types, PathCard> cardConstructor;
    public final boolean negation;

    /**
     * Constructs a CardType with the given card constructor function.
     *
     * @param cardConstructor the function to create a PathCard of this type
     */
    CardType(final BiFunction<Player, Types, PathCard> cardConstructor) {
        this.cardConstructor = (player, filterType) -> {
            final PathCard card = cardConstructor.apply(player, filterType);
            card.setType(this);
            return card;
        };
        negation = false;
    }

    /**
     * Constructs a CardType that negates the given card type.
     *
     * @param cardType the card type to negate
     */
    CardType(final CardType cardType) {
        cardConstructor = (player, filterType) -> {
            final PathCard originalCard = cardType.cardConstructor.apply(player, filterType);
            final PathCard newCard = new PathCard(player,
                    (tile) -> !originalCard.getTileFilterFunction().test(tile),
                    filterType);
            newCard.setType(this);
            return newCard;
        };
        negation = true;
    }
}
