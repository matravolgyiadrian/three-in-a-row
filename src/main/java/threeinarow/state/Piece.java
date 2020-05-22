package threeinarow.state;

/**
 * Enum representing the empty space, the blue and the red pieces on the board.
 */
public enum Piece {
    EMPTY,
    RED,
    BLUE;

    /**
     * Returns the instance represented by the specified value.
     * @param value the value representing an instance
     * @return the instance represented by the specified value
     * @throws IllegalArgumentException if the specified value does not
     * represent an instance
     */
    public static Piece of(int value){
        if(value<0 || value>=values().length){
            throw new IllegalArgumentException();
        }

        return values()[value];
    }

    /**
     * Returns the integer value that represents this instance.
     * @return the integer value that represents this instance
     */
    public int getValue(){
        return ordinal();
    }

    public String toString(){
        return Integer.toString(ordinal());
    }
}
