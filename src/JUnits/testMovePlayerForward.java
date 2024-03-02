package JUnits;


import Model.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testMovePlayerForward {


    @Test
    public void testMovePlayerForward() {

        Player player = new Player(5, "Ali", "Blue", 5);

        player.movePlayerForward(3);

        assertEquals(8, player.getPlayerPosition());
        assertTrue(player.getPlayerPosition()==8);
}}