package pieces;

import java.util.ArrayList;

import src.Board;
import src.Cell;
import src.ChessPiece;

public class King implements ChessPiece{
    private final int VALUE = 999;

    private int value;
    private int color;
    private Cell cell;
    private boolean hasMoved = false;

    public King(int color, Cell cell) {
        this.color = color;
        this.cell = cell;
        setValue(VALUE);
    }

    public static boolean isValidateMove(Cell startCell, Cell endCell, int color) {
        if (endCell.getPiece() != null && (startCell.getPiece().getColor() == endCell.getPiece().getColor())) {
            return false;
        }
        if (startCell.getI() + 1 == endCell.getI() && startCell.getJ() == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() - 1 == endCell.getI() && startCell.getJ() == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() == endCell.getI() && startCell.getJ() + 1 == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() == endCell.getI() && startCell.getJ() - 1 == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() + 1 == endCell.getI() && startCell.getJ() + 1 == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() + 1 == endCell.getI() && startCell.getJ() - 1 == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() - 1 == endCell.getI() && startCell.getJ() + 1 == endCell.getJ()) {
            return true;
        }
        if (startCell.getI() - 1 == endCell.getI() && startCell.getJ() - 1 == endCell.getJ()) {
            return true;
        }
        if(!startCell.getPiece().hasMoved()){
            if(startCell.getI() != endCell.getI()) return false;
            Cell[][] board = startCell.getBoard().getBoard();
            // castling to right
            if(startCell.getJ() - endCell.getJ() == -2){
                for(int i = startCell.getJ() + 1; i < board.length; i++){
                    Cell c = board[startCell.getI()][i];
                    if(c.isEmpty()) continue;
                    if(c.getPiece().getPiece() != ChessPiece.ROOK || c.getPiece().getColor() != color || c.getPiece().hasMoved()) return false;
                } 
                return true; 
            }
            // castling to left
            if(startCell.getJ() - endCell.getJ() == 2){
                for(int i = startCell.getJ() - 1; i >= 0; i--){
                    Cell c = board[startCell.getI()][i];
                    if(c.isEmpty()) continue;
                    if(c.getPiece().getPiece() != ChessPiece.ROOK || c.getPiece().getColor() != color || c.getPiece().hasMoved()) return false;
                } 
                return true; 
            }
        }
        return false;        
    }

    @Override
    public boolean isValidateMove(Cell endCell) {
        return isValidateMove(getCell(), endCell, getColor());
    }

    @Override
    public boolean move(Cell endCell) {
        if (isValidateMove(endCell)) {
            //castling
            if(Math.abs(endCell.getJ() - cell.getJ()) != 1){
                Cell[][] board = cell.getBoard().getBoard();
                ChessPiece rook = null;
                // castling to right
                if(cell.getJ() - endCell.getJ() == -2){
                    for(int i = cell.getJ() + 1; i < board.length; i++){
                        Cell c = board[cell.getI()][i];
                        if(c.isEmpty()) continue;
                        if(c.getPiece().getPiece() == ChessPiece.ROOK){
                            rook = c.getPiece();
                            break;
                        }
                    } 
                    if(rook == null) return false;
                    rook.setCell(board[cell.getI()][cell.getJ() + 1]);
                }
                // castling to left
                if(cell.getJ() - endCell.getJ() == 2){
                    for(int i = cell.getJ() - 1; i >= 0; i--){
                        Cell c = board[cell.getI()][i];
                        if(c.isEmpty()) continue;
                        if(c.getPiece().getPiece() == ChessPiece.ROOK){
                            rook = c.getPiece();
                            break;
                        }
                    }
                    if(rook == null) return false;
                    rook.setCell(board[cell.getI()][cell.getJ() - 1]); 
                    
                }
            }

            endCell.setPiece(this);
            getCell().setPiece(null);
            setCell(endCell);
            hasMoved = true;
            return true;
        } 
        return false;    
    }

    @Override
    public boolean isUnderAttack() {
        Cell[][] cells = cell.getBoard().getBoard();
        for(Cell c[] : cells){
            for(Cell cell : c){
                ChessPiece p = cell.getPiece();
                if(cell.isOccupied() && p.getColor() != getColor()){
                    if(p.isValidateMove(getCell())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int getPiece() {
        return ChessPiece.KING;
    }

    
    @Override
    public Cell getCell() {
        return cell;
    }
    
    @Override
    public void setCell(Cell cell) {
        this.cell.setPiece(null);
        this.cell = cell;
        this.cell.setPiece(this);
    }
    
    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getColor() == ChessPiece.BLACK_COLOR ? "K" : "k";
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    } 

    @Override
    public int[][] getPossibleMovesMatrix() {
        //make a int[][] with sizes of the Board
        //fill it with ChessPiece.POSSIBLE_TO_MOVE if the move is valid
        //fill it with ChessPiece.IMPOSSIBLE_TO_MOVE if the move is invalid
        //fill it with ChessPiece.POSSIBLE_TO_ATTACK if the move is valid and capture a piece
        //fill it with ChessPiece.YOURSELF if it is your piece

        int[][] moves = new int[getCell().getBoard().getBoard().length][getCell().getBoard().getBoard()[0].length];
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                
                if(cell.getI() == i && cell.getJ() == j){
                    moves[i][j] = ChessPiece.YOURSELF;
                    continue;
                }  

                if (isValidateMove(getCell().getBoard().getCell(i, j))) {
                    if (getCell().getBoard().getCell(i, j).isOccupied()) {
                        moves[i][j] = ChessPiece.POSSIBLE_TO_ATTACK;
                    } else {
                        moves[i][j] = ChessPiece.POSSIBLE_TO_MOVE;
                    }
                } else {
                    moves[i][j] = ChessPiece.IMPOSSIBLE_TO_MOVE;
                }
            }
        }
        return moves;
    }

    @Override
    public boolean hasPossibleMoves() {
        int[][] moves = getPossibleMovesMatrix();
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                if (moves[i][j] == ChessPiece.POSSIBLE_TO_MOVE || moves[i][j] == ChessPiece.POSSIBLE_TO_ATTACK) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<Cell> getPossibleMoves() {
        ArrayList<Cell> moves = new ArrayList<Cell>();
        int[][] movesMatrix = getPossibleMovesMatrix();
        for (int i = 0; i < movesMatrix.length; i++) {
            for (int j = 0; j < movesMatrix[i].length; j++) {
                if (movesMatrix[i][j] == ChessPiece.POSSIBLE_TO_MOVE || movesMatrix[i][j] == ChessPiece.POSSIBLE_TO_ATTACK) {
                    moves.add(getCell().getBoard().getCell(i, j));
                }
            }
        }
        return moves;
    }

    public boolean isInCheckMate() {
        if (isUnderAttack()) {
            if (!hasPossibleMoves()) {
                return true;
            }
        }
        return false;
    }

    
    
}
