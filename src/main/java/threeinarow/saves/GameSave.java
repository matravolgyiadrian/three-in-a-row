package threeinarow.saves;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Class representing the state of an unfinished game.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameSave {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the red player.
     */
    @Column(nullable = false, name = "redPlayerName")
    private String redPlayerName;

    /**
     * The name of the blue player.
     */
    @Column(nullable = false, name = "bluePlayerName")
    private String bluPlayerName;

    /**
     * String that represents the unfinished game state.
     */
    @Column(nullable = false)
    private String state;

    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false, name = "created")
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }
}
