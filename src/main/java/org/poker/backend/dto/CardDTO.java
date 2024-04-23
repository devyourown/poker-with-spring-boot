package org.poker.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poker.domain.card.Suit;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private Suit suit;
    private int value;
}
