package sk.tuke.kpi.kp.rollingCube.game.core;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Map {
    private int rowCount;
    private int columnCount;
    private  int[][] field;
    private final List<Integer> dice;
    private final File file;
    private final Point start;
    private final Point end;

    public Map(String fileName) {
        this.file = new File(fileName);
        this.rowCount = 0;
        this.columnCount = 0;
        this.dice = new ArrayList<>();
        this.start = new Point();
        this.end = new Point();
        readFile();
    }

    private void readFile() {
        try {
            Scanner scan = new Scanner(file);

            setRowCount(scan.nextInt());
            setColumnCount(scan.nextInt());

            this.field = new int[getRowCount()][getColumnCount()];

            getDiceSides(scan);
            getStartEndPosition(scan);
            fillField(scan);

        } catch (FileNotFoundException e) {
            System.out.println("file missing!");
        }
    }

    private void getStartEndPosition(Scanner scan) {
        getStart().setLocation(scan.nextInt(), scan.nextInt());
        getEnd().setLocation(scan.nextInt(), scan.nextInt());
    }

    private void getDiceSides(Scanner scan) {
        for(int i=0; i<5; i++)
            if(scan.hasNext())
                getDice().add(scan.nextInt());
    }

    private void fillField(Scanner scan) {
        for(int i=0; i<getRowCount(); i++) {
            for(int j=0; j<getColumnCount(); j++) {
                if(scan.hasNext())
                    getField()[i][j] = scan.nextInt();
            }
        }
    }

    public long folderFileSum() {
        File folder = new File("Levels");
        int fileCount = 0;

        for(File file: Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile())
                fileCount++;
        }
        return fileCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    private void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    private void setColumnCount(int column) {
        this.columnCount = column;
    }

    public int[][] getField() {
        return field;
    }

    public List<Integer> getDice() {
        return dice;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
