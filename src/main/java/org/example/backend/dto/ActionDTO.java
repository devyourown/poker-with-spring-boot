package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.game.Action;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDTO {
    private String gameId;
    private Action action;
    private int betSize;
}
