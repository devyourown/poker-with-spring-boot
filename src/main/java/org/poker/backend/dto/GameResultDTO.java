package org.poker.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poker.domain.player.Player;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResultDTO {
    private List<HandsDTO> allOfHands;
    private List<Player> winner;
}
