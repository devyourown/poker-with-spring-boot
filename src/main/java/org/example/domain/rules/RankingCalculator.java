package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankingCalculator {

    public static long calculateCards(List<Card> cards) {
        List<Integer> numbersOfCards = convertToNumber(cards);
        Collections.sort(numbersOfCards);
        if (getStraightFlush(cards) > 0)
            return getStraightFlush(cards);
        else if (getFourCards(numbersOfCards) > 0)
            return getFourCards(numbersOfCards);
        else if (getFullHouse(numbersOfCards) > 0)
            return getFullHouse(numbersOfCards);
        else if (getFlush(cards) > 0)
            return getFlush(cards);
        else if (getStraight(numbersOfCards) > 0)
            return getStraight(numbersOfCards);
        else if (getTriple(numbersOfCards) > 0)
            return getTriple(numbersOfCards);
        else if (getTwoPair(numbersOfCards) > 0)
            return getTwoPair(numbersOfCards);
        else if (getOnePair(numbersOfCards) > 0)
            return getOnePair(numbersOfCards);
        return getHighCard(numbersOfCards);
    }

    private static int getHighCard(List<Integer> numbers) {
        numbers.sort(Collections.reverseOrder());
        int result = 0;
        for (int i=0; i<5; i++) {
            result += numbers.get(i);
        }
        return result;
    }

    private static List<Integer> convertToNumber(List<Card> cards) {
        List<Integer> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(card.getValue());
        }
        return result;
    }

    private static long getStraightFlush(List<Card> cards) {
        Suit mostSuit = getMostSuited(cards);
        List<Card> sorted = cards.stream().filter(card -> card.getSuit() == mostSuit)
                .sorted(Comparator.comparingInt(Card::getValue))
                .collect(Collectors.toList());
        if (sorted.size() < 5)
            return 0;
        List<Integer> numbers = convertToNumber(sorted);
        for (int i=numbers.size()-5; i>=0; i--) {
            if (isStraight(numbers, i, i+5))
                return Ranking.STRAIGHT_FLUSH.getValue() + numbers.get(i+4);
        }
        if (numbers.get(numbers.size()-1) == 14) {
            if (isLowAceStraight(numbers))
                return Ranking.STRAIGHT_FLUSH.getValue() + 5;
        }
        return 0;
    }

    private static Suit getMostSuited(List<Card> cards) {
        Suit result = Suit.CLUBS;
        int most = 0;
        for (Card card : cards) {
            int count = (int) cards.stream().filter(c -> c.getSuit() == card.getSuit()).count();
            if (most < count) {
                most = count;
                result = card.getSuit();
            }
        }
        return result;
    }

    private static long getFourCards(List<Integer> cardNumbers) {
        int count = sameCountAsExpected(cardNumbers, 4);
        if (count == 0)
            return 0;
        int highCard = getListWithoutSame(cardNumbers, 4).stream()
                .mapToInt(a -> a).max().orElse(0);
        return Ranking.FOUR_CARDS.getValue() + sameCountAsExpected(cardNumbers, 4) * 2L + highCard;
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

    private static long getFullHouse(List<Integer> cardNumbers) {
        List<Integer> leftWithoutTriple = getListWithoutSame(cardNumbers, 3);
        if (leftWithoutTriple.isEmpty())
            return 0;
        List<Integer> triple = getHighestTriple(cardNumbers);
        int pairValue = getValueOfSamePair(leftWithoutTriple);
        if (pairValue > 0)
            return Ranking.FULL_HOUSE.getValue() + triple.get(0) * 3L + pairValue * 2L;
        return 0;
    }

    private static int getValueOfSamePair(List<Integer> numbers) {
        int result = 0;
        for (int number : numbers) {
            int count = (int) numbers.stream().filter(n -> n == number).count();
            if (count >= 2)
                result = Math.max(result, number);
        }
        return result;
    }

    private static List<Integer> getHighestTriple(List<Integer> numbers) {
        List<Integer> result = Collections.emptyList();
        for (int number : numbers) {
            int count = (int) numbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == 3)
                result = numbers.stream()
                        .filter(cardNumber -> cardNumber == number)
                        .collect(Collectors.toList());
        }
        return result;
    }

    private static List<Integer> getListWithoutSame(List<Integer> numbers, int sameCount) {
        List<Integer> result = Collections.emptyList();
        for (int number : numbers) {
            int count = (int) numbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == sameCount)
                result = numbers.stream()
                        .filter(cardNumber -> cardNumber != number)
                        .collect(Collectors.toList());
        }
        return result;
    }

    private static long getFlush(List<Card> cards) {
        if (isFlush(cards)) {
            Suit mostSuit = getMostSuited(cards);
            int highValue = cards.stream().filter(card -> card.getSuit().equals(mostSuit))
                    .mapToInt(Card::getValue)
                    .max().orElse(0);
            return Ranking.FLUSH.getValue() + highValue;
        }
        return 0;
    }

    private static boolean isFlush(List<Card> cards) {
        for (Card card : cards) {
            int count = (int) cards.stream().filter(c -> c.getSuit() == card.getSuit()).count();
            if (count >= 5)
                return true;
        }
        return false;
    }

    private static long getStraight(List<Integer> cardNumbers) {
        cardNumbers = cardNumbers.stream().distinct().collect(Collectors.toList());
        for (int i=cardNumbers.size()-5; i>=0; i--) {
            if (isStraight(cardNumbers, i, i+5))
                return Ranking.STRAIGHT.getValue() + cardNumbers.get(i+4);
        }
        if (cardNumbers.get(cardNumbers.size()-1) == 14) {
            if (isLowAceStraight(cardNumbers))
                return Ranking.STRAIGHT.getValue() + 5;
        }
        return 0;
    }

    private static boolean isStraight(List<Integer> numbers, int start, int end) {
        for (int i=start+1; i<end; i++) {
            if (numbers.get(i-1) + 1 != numbers.get(i))
                return false;
        }
        return true;
    }

    private static boolean isLowAceStraight(List<Integer> cards) {
        if (cards.get(0) != 2)
            return false;
        for (int i=1; i<4; i++) {
            if (cards.get(i-1)+ 1 != cards.get(i))
                return false;
        }
        return true;
    }

    private static long getTriple(List<Integer> cardNumbers) {
        int count = sameCountAsExpected(cardNumbers, 3);
        if (count == 0)
            return 0;
        List<Integer> sorted = cardNumbers.stream().filter(c -> c != count)
                .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        return Ranking.TRIPLE.getValue() + sameCountAsExpected(cardNumbers, 3) * 3L
                + sorted.get(0) * 2 + sorted.get(1);
    }

    private static long getTwoPair(List<Integer> cardNumbers) {
        List<Integer> result = new ArrayList<>();
        int highCard = 0;
        for (int number : cardNumbers) {
            int count = (int) cardNumbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == 2)
                result.add(number);
            else {
                if (highCard < number)
                    highCard = number;
            }
        }
        if (result.size() < 4)
            return 0;
        List<Integer> sorted = result.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
        return Ranking.TWO_PAIR.getValue() + sorted.get(0) * 3L
                + sorted.get(2) * 2 + highCard;
    }

    private static long getOnePair(List<Integer> cardNumbers) {
        for (int number : cardNumbers) {
            int count = (int) cardNumbers.stream()
                    .filter(cardNumber -> cardNumber == number)
                    .count();
            if (count == 2) {
                int leftOver = 0;
                int numOfLeft = 0;
                for (int i=cardNumbers.size()-1; i>=0; i--) {
                    if (number != cardNumbers.get(i)) {
                        leftOver += cardNumbers.get(i);
                        numOfLeft += 1;
                    }
                    if (numOfLeft == 3) break;
                }
                return Ranking.ONE_PAIR.getValue() + number * 3L + leftOver;
            }
        }
        return 0;
    }

}
