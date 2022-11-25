package org.example.domain;

import org.example.domain.card.CardTest;
import org.example.domain.deck.DeckTest;
import org.example.domain.game.GameResultTest;
import org.example.domain.game.GameTest;
import org.example.domain.player.PlayerTest;
import org.example.domain.room.RoomTest;
import org.example.domain.rules.RankingCalculatorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({RankingCalculatorTest.class, RoomTest.class,
        PlayerTest.class, GameTest.class, GameResultTest.class,
        DeckTest.class, CardTest.class})
public class GroupTest {
}
