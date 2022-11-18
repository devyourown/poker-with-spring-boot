package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RankingCalculator {
    public static HandRanking calculateHands(List<Card> hands) {
        if (hands.get(0).isSameValue(hands.get(1)))
            return HandRanking.ONE_PAIR;
        return HandRanking.HIGH_CARD;
    }

    public static HandRanking calculateCards(List<Card> cards) {
        List<Integer> numbersOfCards = convertToNumber(cards);
        List<Suit> suitsOfCards = convertToSuit(cards);
        if (isRoyalStraightFlush(cards))
            return HandRanking.ROYAL_STRAIGHT_FLUSH;
        else if (isStraightFlush(cards))
            return HandRanking.STRAIGHT_FLUSH;
        else if (isFourCards(numbersOfCards))
            return HandRanking.FOUR_CARDS;
        else if (isFullHouse(numbersOfCards))
            return HandRanking.FULL_HOUSE;
        else if (isFlush(suitsOfCards))
            return HandRanking.FLUSH;
        else if (isMountain(numbersOfCards))
            return HandRanking.MOUNTAIN;
        else if (isStraight(numbersOfCards))
            return HandRanking.STRAIGHT;
        else if (isTriple(numbersOfCards))
            return HandRanking.TRIPLE;
        else if (isTwoPair(numbersOfCards))
            return HandRanking.TWO_PAIR;
        else if (isOnePair(numbersOfCards))
            return HandRanking.ONE_PAIR;
        return HandRanking.HIGH_CARD;
    }

    private static List<Integer> convertToNumber(List<Card> cards) {
        List<Integer> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(card.getValue());
        }
        return result;
    }

    private static List<Suit> convertToSuit(List<Card> cards) {
        List<Suit> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(card.getSuit());
        }
        return result;
    }

    private static boolean isRoyalStraightFlush(List<Card> cards) {
        List<Card> copied = new ArrayList<>(cards);
        for (Card card : cards) {
            if (card.isAce()) {
                copied.add(new Card(14, card.getSuit()));
                copied.remove(card);
            }
        }
        for (Suit suit : Suit.values()) {
            int count = (int) copied.stream()
                    .filter(card -> card.getSuit() == suit)
                    .count();
            if (count >= 5)
                return copied.stream()
                        .filter(card -> card.getValue() >= 10)
                        .filter(card -> card.getSuit() == suit)
                        .count() == 5;
        }
        return false;
    }

    private static boolean isStraightFlush(List<Card> cards) {
        List<Card> sorted = cards.stream()
                .sorted((a, b) -> a.getValue() - b.getValue())
                .collect(Collectors.toList());
        for (int i=1; i<sorted.size(); i++) {
            if (sorted.get(i-1).getValue() != sorted.get(i).getValue() - 1)
                if (i >= cards.size() - 4)
                    return false;
            if (sorted.get(i-1).getSuit() != sorted.get(i).getSuit())
                if (i >= cards.size() - 4)
                    return false;
        }
        return true;
    }

    private static boolean isFourCards(List<Integer> cardNumbers) {
        if (sameCountAsExpected(cardNumbers, 4))
            return true;
        return false;
    }

    private static boolean sameCountAsExpected(List<Integer> cardNumbers, int expected) {
        for (int number : cardNumbers) {
            int count = (int) cardNumbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == expected)
                return true;
        }
        return false;
    }

    private static boolean isFullHouse(List<Integer> cardNumbers) {
        List<Integer> leftWithoutTriple = getListWithoutTriple(cardNumbers);
        if (leftWithoutTriple.isEmpty())
            return false;
        if (!isOnePair(leftWithoutTriple) && !isTriple(leftWithoutTriple))
            return false;
        return true;
    }

    private static List<Integer> getListWithoutTriple(List<Integer> numbers) {
        List<Integer> result = Collections.emptyList();
        for (int number : numbers) {
            int count = (int) numbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == 3)
                result = numbers.stream()
                        .filter(cardNumber -> cardNumber != number)
                        .collect(Collectors.toList());
        }
        return result;
    }

    private static boolean isFlush(List<Suit> cardSuits) {
        for (Suit suit : Suit.values()) {
            int count = (int) cardSuits.stream()
                    .filter(cardSuit -> cardSuit == suit)
                    .count();
            if (count >= 5)
                return true;
        }
        return false;
    }

    private static boolean isMountain(List<Integer> cardNumbers) {
        List<Integer> sorted = cardNumbers.stream()
                .filter(num -> num >= 2)
                .collect(Collectors.toList());
        if (sorted.size() != cardNumbers.size())
            sorted.add(14);
        sorted = sorted.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        if (sorted.indexOf(10) == -1)
            return false;
        if (sorted.size() - sorted.indexOf(10) != 5)
            return false;
        return true;
    }

    private static boolean isStraight(List<Integer> cardNumbers) {
        List<Integer> sorted = cardNumbers.stream()
                .distinct().sorted()
                .collect(Collectors.toList());
        if (sorted.size() < 5)
            return false;
        for (int i=1; i<sorted.size(); i++) {
            if (sorted.get(i-1) != sorted.get(i) - 1)
                if (i >= cardNumbers.size() - 4)
                    return false;
        }
        return true;
    }

    private static boolean isTriple(List<Integer> cardNumbers) {
        if (sameCountAsExpected(cardNumbers, 3))
            return true;
        return false;
    }

    private static boolean isTwoPair(List<Integer> cardNumbers) {
        return cardNumbers.stream().distinct().count() == cardNumbers.size() - 2;
    }

    private static boolean isOnePair(List<Integer> cardNumbers) {
        return cardNumbers.stream().distinct().count() == cardNumbers.size() - 1;
    }

}
