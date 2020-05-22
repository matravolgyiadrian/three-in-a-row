package threeinarow.state;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ThreeInARowStateTest {

    @Test
    void testOneArgConstructor_InvalidArg() {
        assertThrows(IllegalArgumentException.class, () -> new ThreeInARowState(null));
        assertThrows(IllegalArgumentException.class, () -> new ThreeInARowState(new int[][]{
                {1, 2},
                {2, 0}})
        );
        assertThrows(IllegalArgumentException.class, () -> new ThreeInARowState(new int[][]{
                {0, 0, 0, 1},
                {1, 2, 0, 2},
                {0, 0, 1, 0}})
        );
        assertThrows(IllegalArgumentException.class, () -> new ThreeInARowState(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 0, 2},
                {0, 1, 2, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 0}})
        );
        assertThrows(IllegalArgumentException.class, () -> new ThreeInARowState(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 0, 2},
                {0, 1, 2, 0},
                {1, 0, 2, 0},
                {0, 3, 0, 2}})
        );
        assertThrows(IllegalArgumentException.class, () -> new ThreeInARowState(new int[][]{
                {0, 0, 1, 0},
                {1, 0, 0, 2},
                {0, 2, 2, 0},
                {1, 0, 0, 0},
                {0, 0, 0, 0}})
        );
    }

    @Test
    void testOneArgConstructor_ValidArg() {
        int[][] board = new int[][] {
                {2, 1, 2, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 2, 1, 2}
        };
        ThreeInARowState state = new ThreeInARowState(board);
        assertArrayEquals(new Piece[][] {
                {Piece.BLUE, Piece.RED, Piece.BLUE, Piece.RED},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.RED, Piece.BLUE, Piece.RED, Piece.BLUE}
        }, state.getBoard());
    }

    @Test
    void testIsEnd(){
        assertFalse(new ThreeInARowState().isEnd());
        assertTrue(new ThreeInARowState(new int[][] {
            {0, 0, 0, 2},
            {2, 1, 1, 1},
            {2, 0, 0, 1},
            {0, 2, 0, 0},
            {0, 0, 0, 0}
        }).isEnd());
        assertTrue(new ThreeInARowState(new int[][] {
            {0, 1, 0, 2},
            {2, 0, 1, 1},
            {2, 0, 0, 1},
            {0, 2, 0, 0},
            {0, 0, 0, 0}
        }).isEnd());
        assertTrue(new ThreeInARowState(new int[][] {
            {0, 0, 0, 2},
            {2, 0, 1, 1},
            {2, 0, 1, 1},
            {2, 0, 0, 0},
            {0, 0, 0, 0}
        }).isEnd());
    }

    @Test
    void testWhereToMove(){
        ThreeInARowState state = new ThreeInARowState();
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.UP);
        assertEquals(directions, state.whereToMove(4, 3));

        state.moveTo(4,3, Direction.UP);
        directions.add(Direction.DOWN); directions.add(Direction.LEFT);
        assertEquals(directions, state.whereToMove(3, 3));
    }

    @Test
    void testMoveTo(){
        ThreeInARowState state = new ThreeInARowState();
        state.moveTo(4, 3, Direction.UP);
        assertTrue(Arrays.deepEquals(new Piece[][]{
                {Piece.BLUE, Piece.RED, Piece.BLUE, Piece.RED},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.BLUE},
                {Piece.RED, Piece.BLUE, Piece.RED, Piece.EMPTY}
        }, state.getBoard()));

        state.moveTo(4, 2, Direction.RIGHT);
        assertTrue(Arrays.deepEquals(new Piece[][]{
                {Piece.BLUE, Piece.RED, Piece.BLUE, Piece.RED},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY},
                {Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.BLUE},
                {Piece.RED, Piece.BLUE, Piece.EMPTY, Piece.RED}
        }, state.getBoard()));

        assertThrows(IllegalArgumentException.class, () -> state.moveTo(3, 3, Direction.DOWN)); //the piece can not move the specified direction
        assertThrows(IllegalArgumentException.class, () -> state.moveTo(4, 3, Direction.LEFT)); //the piece can move to the specified direction but it's not his turn
    }

    @Test
    void testToString(){
        ThreeInARowState state = new ThreeInARowState();
        assertEquals("2 1 2 1 \n"
                            +"0 0 0 0 \n"
                            +"0 0 0 0 \n"
                            +"0 0 0 0 \n"
                            +"1 2 1 2 \n", state.toString());
    }
}
