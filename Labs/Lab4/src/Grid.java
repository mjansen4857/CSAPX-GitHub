/**
 * This class contains a 2D array that stores all values from the KenKen puzzle
 *
 * @author Michael Jansen
 */
public class Grid {
    private int grid[][];

    /**
     * Grid constructor
     * @param dimension The square dimension of the grid
     */
    public Grid(int dimension){
        this.grid = new int[dimension][dimension];
    }

    /**
     * Sets a value in the grid
     * @param row The row location
     * @param col The column location
     * @param val The value to set at the given location
     */
    public void setValue(int row, int col, int val){
        this.grid[row][col] = val;
    }

    /**
     * Gets a value from the grid
     * @param row The row location
     * @param col The column location
     * @return The value at the given location
     */
    public int getValue(int row, int col){
        return this.grid[row][col];
    }
}
