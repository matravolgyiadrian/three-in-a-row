package threeinarow.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PieceTest {

    @Test
    public void testOf(){
        assertThrows(IllegalArgumentException.class, () -> Piece.of(-1));
        assertEquals(Piece.EMPTY, Piece.of(0));
        assertEquals(Piece.RED, Piece.of(1));
        assertEquals(Piece.BLUE, Piece.of(2));
        assertThrows(IllegalArgumentException.class, () -> Piece.of(3));
    }




}