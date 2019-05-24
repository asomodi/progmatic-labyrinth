package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {

    CellType[][] labyrinth;
    Coordinate playerPosition;

    public LabyrinthImpl() {
        loadLabyrinthFile("labyrinth1.txt");

    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File("labyrinth1.txt"));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());

            CellType[][] labFill = new CellType[width][height];
            labyrinth = new CellType[width][height];

            for (int hh = 0; hh < width; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < height; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labFill[hh][ww] = CellType.WALL;
                            break;
                        case 'E':
                            labFill[hh][ww] = CellType.END;
                            break;
                        case 'S':
                            labFill[hh][ww] = CellType.START;
                            break;
                        case ' ':
                            labFill[hh][ww] = CellType.EMPTY;
                            break;
                    }
                }
            }
            labyrinth = labFill;
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }

    }

    @Override
    public int getWidth() {
        if (labyrinth == null) {
            return -1;
        } else {
            return labyrinth.length;
        }
    }

    @Override
    public int getHeight() {
        if (labyrinth == null) {
            return -1;
        } else {
            return labyrinth[0].length;
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {

        if (c == null) {
            throw new CellException(c, "This coordinate doesn't exists.");
        } else {
            return labyrinth[c.getRow()][c.getCol()];
        }

    }

    @Override
    public void setSize(int width, int height) {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                labyrinth[i][j] = CellType.EMPTY;

            }
        }

    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if (c == null) {
            throw new CellException(c, "This coordinate doesn't exists.");
        } else if (type == CellType.START) {
            c = playerPosition;
        }
    }

    @Override
    public Coordinate getPlayerPosition() {

        return playerPosition;
    }

    @Override
    public boolean hasPlayerFinished() {
        if (labyrinth[getPlayerPosition().getRow()][getPlayerPosition().getCol()] == CellType.END) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Direction> possibleMoves() {
        ArrayList<Direction> possibleMoves = new ArrayList<Direction>();

        if (labyrinth[getPlayerPosition().getRow() + 1][getPlayerPosition().getCol()] != CellType.WALL
                && getPlayerPosition().getRow() >= 1) {
            possibleMoves.add(Direction.EAST);
        }
        if (labyrinth[getPlayerPosition().getRow() - 1][getPlayerPosition().getCol()] != CellType.WALL
                && getPlayerPosition().getRow() < labyrinth.length - 1) {
            possibleMoves.add(Direction.WEST);
        }
        if (labyrinth[getPlayerPosition().getRow()][getPlayerPosition().getCol() - 1] != CellType.WALL
                && getPlayerPosition().getCol() >= 1) {
            possibleMoves.add(Direction.NORTH);
        }
        if (labyrinth[getPlayerPosition().getRow()][getPlayerPosition().getCol() + 1] != CellType.WALL
                && getPlayerPosition().getCol() <= labyrinth[getPlayerPosition().getCol()].length - 1) {
            possibleMoves.add(Direction.SOUTH);
        }

        return possibleMoves;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        if (!possibleMoves().contains(direction)) {
            throw new InvalidMoveException();
        } else {
            switch (direction) {
                case SOUTH:
                    playerPosition = new Coordinate(playerPosition.getRow() + 1, playerPosition.getCol());
                    break;
                case NORTH:
                    playerPosition = new Coordinate(playerPosition.getRow() - 1, playerPosition.getCol());
                    break;
                case WEST:
                    playerPosition = new Coordinate(playerPosition.getRow(), playerPosition.getCol() + 1);
                    break;
                case EAST:
                    playerPosition = new Coordinate(playerPosition.getRow(), playerPosition.getCol() - 1);
                    break;
            }
        }
    }

}
