package hProjekt.model.cards;

/**
 * Represents a {@link GoldCard} containing gold.
 * <p>
 * This record holds the specific value of the gold associated with the card.
 *
 * @param value The amount of gold this card is worth.
 */
public record GoldCard(int value) implements TreasureCard {}
