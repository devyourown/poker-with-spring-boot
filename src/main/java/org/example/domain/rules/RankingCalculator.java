package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RankingCalculator {

    public static int calculateCards(List<Card> cards) {
        List<Integer> numbersOfCards = convertToNumber(cards);
        List<Suit> suitsOfCards = convertToSuit(cards);
        if (getStraightFlush(cards) > 0)
            return getStraightFlush(cards);
        else if (getFourCards(numbersOfCards) > 0)
            return getFourCards();
        else if (isFullHouse(numbersOfCards))
            return Ranking.FULL_HOUSE;
        else if (isFlush(suitsOfCards))
            return Ranking.FLUSH;
        else if (isStraight(numbersOfCards))
            return Ranking.STRAIGHT;
        else if (isTriple(numbersOfCards))
            return Ranking.TRIPLE;
        else if (isTwoPair(numbersOfCards))
            return Ranking.TWO_PAIR;
        else if (isOnePair(numbersOfCards))
            return Ranking.ONE_PAIR;
        return Ranking.HIGH_CARD;
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

    private static int getStraightFlush(List<Card> cards) {
        List<Card> sorted = cards.stream().sorted().collect(Collectors.toList());
        for (int i=0; i<3; i++) {
            if (isStraightFlush(sorted, i, i+5))
                return Ranking.STRAIGHT_FLUSH.getValue() + sorted.get(i+4).getValue();
        }
        return 0;
    }

    private static boolean isStraightFlush(List<Card> cards, int start, int end) {
        for (int i=start+1; i<end; i++) {
            if (cards.get(i-1).getValue() + 1 != cards.get(i).getValue())
                return false;
            if (cards.get(i-1).getSuit() != cards.get(i).getSuit())
                return false;
        }
        return true;
    }

    private static int getFourCards(List<Integer> cardNumbers) {
        return Ranking.FOUR_CARDS.getValue() + sameCountAsExpected(cardNumbers, 4);
    }

    private static int sameCountAsExpected(List<Integer> cardNumbers, int expected) {
        for (int number : cardNumbers) {
            int count = (int) cardNumbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == expected)
                return number;
        }
        return 0;
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
