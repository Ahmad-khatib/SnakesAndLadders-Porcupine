package JUnits;

import Model.Player;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerEqualsTest {
    Player P = new Player(22, "nour", "green", 18);

    Player P2 = new Player(22, "nour", "green", 18);

            Player P3 = new Player(33, "majd", "blue", 32);

    @Test
    public void CheckTwoEqualPlayers(){
        assertTrue(P.equals(P2));

    }
    @Test
    public void CheckTwoNotEqualPlayers(){
        assertFalse(P.equals(P3));

}
}