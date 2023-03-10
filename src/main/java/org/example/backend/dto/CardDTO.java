package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.card.Suit;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private Suit suit;
    private int value;
}
