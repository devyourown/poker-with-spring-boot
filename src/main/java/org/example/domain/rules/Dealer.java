package org.example.domain.rules;

import org.example.domain.card.Card;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Dealer {

    public static HandRanking calculateCards(List<Card> cards) {
        List<Integer> numbersOfCards = convertToNumber(cards);
        if (isMountain(numbersOfCards))
            return HandRanking.MOUNTAIN;
        if (isStraight(numbersOfCards))
            return HandRanking.STRAIGHT;
        if (isTriple(numbersOfCards))
            return HandRanking.TRIPLE;
        if (isTwoPair(numbersOfCards))
            return HandRanking.TWO_PAIR;
        if (isOnePair(numbersOfCards))
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

    public static HandRanking calculateHands(List<Card> hands) {
        if (hands.get(0).isSameAs(hands.get(1)))
            return HandRanking.ONE_PAIR;
        return HandRanking.HIGH_CARD;
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
        for (int i=1; i<sorted.size(); i++) {
            if (sorted.get(i - 1) != sorted.get(i) - 1)
                if (i >= cardNumbers.size() - 4)
                    return false;
        }
        return true;
    }

    private static boolean isStraight(List<Integer> cardNumbers) {
        List<Integer> sorted = cardNumbers.stream()
                .distinct().sorted()
                .collect(Collectors.toList());
        for (int i=1; i<sorted.size(); i++) {
            if (sorted.get(i-1) != sorted.get(i) - 1)
                if (i >= cardNumbers.size() - 4)
                    return false;
        }
        return true;
    }

    private static boolean isTriple(List<Integer> cardNumbers) {
        for (int number : cardNumbers) {
            int count = (int) cardNumbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == 3)
                return true;
        }
        return false;
    }

    private static boolean isTwoPair(List<Integer> cardNumbers) {
        return cardNumbers.stream().distinct().count() == cardNumbers.size() - 2;
    }

    private static boolean isOnePair(List<Integer> cardNumbers) {
        return cardNumbers.stream().distinct().count() == cardNumbers.size() - 1;
    }

}
