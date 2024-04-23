package org.example.domain.rules;

import org.example.domain.card.Card;
import org.example.domain.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankingCalculator {

    public static int calculateCards(List<Card> cards) {
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

    private static List<Suit> convertToSuit(List<Card> cards) {
        List<Suit> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(card.getSuit());
        }
        return result;
    }

    private static int getStraightFlush(List<Card> cards) {
        List<Card> sorted = cards.stream().sorted(Comparator.comparingInt(Card::getValue))
                .collect(Collectors.toList());
        for (int i=2; i>=0; i--) {
            if (isStraightFlush(sorted, i, i+5))
                return Ranking.STRAIGHT_FLUSH.getValue() + sorted.get(i+4).getValue();
        }
        if (cards.get(cards.size()-1).getValue() == 14) {
            if (isLowAceStraightFlush(cards))
                return Ranking.STRAIGHT_FLUSH.getValue() + 5;
        }
        return 0;
    }

    private static boolean isLowAceStraightFlush(List<Card> cards) {
        if (cards.get(0).getValue() != 2 || cards.get(0).getSuit() != cards.get(cards.size()-1).getSuit())
            return false;
        for (int i=1; i<4; i++) {
            if (cards.get(i-1).getValue() + 1 != cards.get(i).getValue())
                return false;
            if (cards.get(i-1).getSuit() != cards.get(i).getSuit())
                return false;
        }
        return true;
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
        int count = sameCountAsExpected(cardNumbers, 4);
        if (count == 0)
            return 0;
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

    private static int getFullHouse(List<Integer> cardNumbers) {
        List<Integer> leftWithoutTriple = getListWithoutTriple(cardNumbers);
        if (leftWithoutTriple.isEmpty())
            return 0;
        if ((getOnePair(leftWithoutTriple) > 0 && getTriple(cardNumbers) > 0))
            return Ranking.FULL_HOUSE.getValue() + getTriple(cardNumbers) + getOnePair(leftWithoutTriple);
        if (getTriple(leftWithoutTriple) > 0 && getTriple(cardNumbers) > 0) {
            if (getTriple(leftWithoutTriple) > getTriple(cardNumbers))
                return Ranking.FULL_HOUSE.getValue() + getTriple(leftWithoutTriple) + getOnePair(cardNumbers);
            return Ranking.FULL_HOUSE.getValue() + getTriple(cardNumbers) + getOnePair(leftWithoutTriple);
        }
        return 0;
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

    private static int getFlush(List<Card> cards) {
        List<Card> sorted = cards.stream().sorted(Comparator.comparingInt(Card::getValue))
                .collect(Collectors.toList());
        for (int i=2; i>=0; i--) {
            if (isFlush(sorted, i, i+5))
                return Ranking.FLUSH.getValue() + sorted.get(i+4).getValue();
        }
        return 0;
    }

    private static boolean isFlush(List<Card> cards, int start, int end) {
        int suited = 0;
        for (int i=start; i<end; i++) {
            Card card = cards.get(i);
            if (card.getSuit() == cards.get(0).getSuit())
                suited += 1;
        }
        return suited == 5;
    }

    private static int getStraight(List<Integer> cardNumbers) {
        for (int i=2; i>=0; i--) {
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

    private static int getTriple(List<Integer> cardNumbers) {
        int count = sameCountAsExpected(cardNumbers, 3);
        if (count == 0)
            return 0;
        return Ranking.TRIPLE.getValue() + sameCountAsExpected(cardNumbers, 3);
    }

    private static int getTwoPair(List<Integer> cardNumbers) {
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
        List<Integer> sorted = result.stream().sorted().collect(Collectors.toList());
        return Ranking.TWO_PAIR.getValue() + sorted.get(sorted.size()-1) + sorted.get(sorted.size()-2) + highCard;
    }

    private static int getOnePair(List<Integer> cardNumbers) {
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
                return Ranking.ONE_PAIR.getValue() + leftOver;
            }
        }
        return 0;
    }

}
