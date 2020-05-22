package threeinarow.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the state of the game.
 */
@Data
@Slf4j
public class ThreeInARowState {

    /**
     * Array representing the starting configuration of the board.
     */
    public static final int[][] INITIAL = {
            {2, 1, 2, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 2, 1, 2}
    };

    /**
     * Creates a {@code ThreeInARowState} object representing the initial state
     * of the game.
     */
    public ThreeInARowState(){
        this(INITIAL);
    }

    /**
     * Creates a {@code ThreeInARowState} object that is initialized it with
     * with the specified array.
     *
     * @param board an array of size 5&#xd7;4 representing the initial configuration
     * of the board
     * @throws IllegalArgumentException if the specified array does not represent a valid
     * configuration of the board
     */
    public ThreeInARowState(int[][] board){
        if(!isValidBoard(board)){
            throw new IllegalArgumentException();
        }
        initBoard(board);
    }

    private boolean isValidBoard(int[][] board){
        if(board == null || board.length != 5){
            return false;
        }
        int countRed=0;
        int countBlue=0;
        for(int[] row:board){
            if(row == null || row.length !=4){
                return false;
            }
            for(int a:row){
                if(a<0 || a>Piece.values().length){
                    return false;
                }
                countBlue += (a==2)?1:0;
                countRed += (a==1)?1:0;
            }
        }
        return countBlue == 4 && countRed == 4;
    }

    private void initBoard(int[][] board){
        this.board = new Piece[5][4];
        for(int i=0; i<5;i++){
            for(int j=0; j<4; j++){
                this.board[i][j] = Piece.of(board[i][j]);
            }
        }
    }

    /**
     * Array holding the current configuration of the board.
     */
    @Setter(AccessLevel.NONE)
    private Piece[][] board;

    /**
     * The piece that wins the game.
     */
    @Setter(AccessLevel.NONE)
    private Piece winner;

    /**
     * The piece which will move
     */
    @Setter(AccessLevel.NONE)
    private Piece turn = Piece.BLUE;

    /**
     * Switches the {@code turn} to that piece which will move next.
     */
    private void nextTurn(){
        if(this.turn == Piece.RED){
            this.turn = Piece.BLUE;
        } else{
            this.turn = Piece.RED;
        }
    }

    /**
     * Moves the piece from the specified position to the specified direction.
     *
     * @param row the row of the piece to be moved
     * @param column the column of the piece to be moved
     * @param direction the direction of the movement
     * @throws IllegalArgumentException if the specified position is empty or the specified
     * direction is impossible on that piece or the piece in the specified position is not the
     * one which will move
     */
    public void moveTo(int row, int column, Direction direction){
        List<Direction> directions = whereToMove(row, column);
        if(this.board[row][column]!=this.turn){
            throw new IllegalArgumentException();
        }
        if(!directions.contains(direction)){
            throw new IllegalArgumentException();
        }
        log.info("The {} piece at ({},{}) is moved to {}", this.board[row][column].name(), row, column, direction);
        swapPieces(row, column, direction);
        nextTurn();
    }

    private void swapPieces(int row, int column, Direction direction){
        Piece temp;
        int newRow = row + direction.getDx();
        int newColumn = column + direction.getDy();
        temp = this.board[row][column];
        this.board[row][column] = this.board[newRow][newColumn];
        this.board[newRow][newColumn] = temp;

    }

    /**
     * Returns a list of {@code Direction} that represent the directions
     * which the piece in the specified position can move to.
     *
     * @param row the row of the examined piece
     * @param column the column of the examined piece
     * @return an list of {@code Direction} that represent the directions
     * which the piece in the specified position can move to
     * @throws IllegalArgumentException if the specified position is empty
     */
    public List<Direction> whereToMove(int row, int column){
        List<Direction> directions = new ArrayList<>();

        if(canMoveUp(row, column)){
            directions.add(Direction.UP);
        }
        if(canMoveDown(row, column)){
            directions.add(Direction.DOWN);
        }
        if(canMoveRight(row, column)){
            directions.add(Direction.RIGHT);
        }
        if(canMoveLeft(row, column)){
            directions.add(Direction.LEFT);
        }

        return directions;
    }

    private boolean canMoveUp(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (row-1)>=0 && (row-1)<5 && this.board[row-1][column]==Piece.EMPTY;
    }

    private boolean canMoveDown(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (row+1)>=0 && (row+1)<5 && this.board[row+1][column]==Piece.EMPTY;
    }

    private boolean canMoveRight(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (column+1)>=0 && (column+1)<4 && this.board[row][column+1]==Piece.EMPTY;
    }

    private boolean canMoveLeft(int row, int column){
        if(this.board[row][column]==Piece.EMPTY){
            throw new IllegalArgumentException();
        }
        return (column-1)>=0 && (column-1)<4 && this.board[row][column-1]==Piece.EMPTY;
    }

    /**
     * Checks whether the game is ended.
     *
     * @return {@code true} if one of the players has 3 piece in a row, column or diagonal,
     * {@code false} otherwise
     */
    public boolean isEnd(){
        return isThreeInARow() || isThreeInAColumn() || isThreeInDiagonal();
    }

    private boolean isThreeInARow(){
        for(Piece[] row:this.board){
            for(int i=0; i<2; i++){
                if(row[i]==row[i+1] && row[i]==row[i+2] && row[i]!=Piece.EMPTY){
                    this.winner = row[i];
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThreeInAColumn(){
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(this.board[i][j]==this.board[i+1][j] && this.board[i][j]==this.board[i+2][j] && this.board[i][j]!=Piece.EMPTY){
                    this.winner = this.board[i][j];
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThreeInDiagonal(){
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 4; j++) {
                if(j+2<4){
                   if(this.board[i][j]==this.board[i+1][j+1] && this.board[i][j]==this.board[i+2][j+2] && this.board[i][j]!=Piece.EMPTY){
                       winner = this.board[i][j];
                       return true;
                   }
                }
                if(j-2>=0){
                    if(this.board[i][j]==this.board[i+1][j-1] && this.board[i][j]==this.board[i+2][j-2] && this.board[i][j]!=Piece.EMPTY){
                        winner = this.board[i][j];
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Piece[] row:this.board){
            for(Piece piece:row){
                sb.append(piece).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }




    public static void main(String[] args) {
        ThreeInARowState state = new ThreeInARowState();
        System.out.println(state);
        state.moveTo(4, 3, Direction.UP);
        System.out.println(state);
        state.moveTo(0, 3, Direction.DOWN);
        System.out.println(state);
    }
}
