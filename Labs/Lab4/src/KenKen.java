import regions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * CSAPX - Lab 4 - KenKen
 *
 * This program verifies whether a KenKen puzzle was solved correctly
 *
 * @author Michael Jansen
 */
public class KenKen {
    private ArrayList<Region> regions;
    private ArrayList<Region> incorrect;

    /**
     * KenKen constructor. Reads a given file and adds all of the region data to array lists
     * @param filename The KenKen file to read
     * @throws FileNotFoundException
     */
    public KenKen(String filename) throws FileNotFoundException {
        this.regions = new ArrayList<>();
        this.incorrect = new ArrayList<>();

        // Create a scanner object using the given file
        File file = new File(filename);
        Scanner in = new Scanner(file);

        // Read the first line and get the puzzle dimension and number of regions
        String[] firstFields = in.nextLine().split(" ");
        int dimension = Integer.parseInt(firstFields[0]);
        int numRegions = Integer.parseInt(firstFields[1]);

        // Create a grid with the same dimensions as the puzzle
        Grid grid = new Grid(dimension);

        // Skip a blank line then add each value in the puzzle to the grid
        in.nextLine();
        for(int i = 0; i < dimension; i++){
            String[] fields = in.nextLine().split(" ");
            for(int j = 0; j < fields.length; j++){
                grid.setValue(i, j, Integer.parseInt(fields[j]));
            }
        }

        // Skip a blank line then add each region to the regions list
        in.nextLine();
        for(int i = 0; i < numRegions; i++){
            String[] fields = in.nextLine().split(" ");
            int target = Integer.parseInt(fields[0]);
            switch (fields[1]){
                case "+":
                    this.regions.add(new AddRegion(i, target));
                    break;
                case "-":
                    this.regions.add(new SubtractRegion(i, target));
                    break;
                case "*":
                    this.regions.add(new MultiplyRegion(i, target));
                    break;
                case "/":
                    this.regions.add(new DivideRegion(i, target));
                    break;
                default:
                    System.err.println("Error: Unknown region type");
            }
        }

        // Skip a blank line then add each value from the grid to its corresponding region
        in.nextLine();
        for(int i = 0; i < dimension; i++){
            String[] fields = in.nextLine().split(" ");
            for(int j = 0; j < fields.length; j++){
                int regionNum = Integer.parseInt(fields[j]);
                this.regions.get(regionNum).addValue(grid.getValue(i, j));
            }
        }
    }

    /**
     * Checks all regions to see if the puzzle is correct. Will print all region data
     * and any incorrect region numbers if they exist.
     */
    public void check(){
        for(Region region:regions){
            if(!region.verify()){
                incorrect.add(region);
            }
            System.out.println(region);
        }

        if(incorrect.size() == 0){
            System.out.println("*** Puzzle is correct!");
        }else{
            System.out.println("*** Puzzle is incorrect! Invalid regions: [" + this.getIncorrectRegions() + "]");
        }
    }

    /**
     * This method is used to create a string containing the incorrect region numbers
     * separated by commas
     * @return The comma-separated region numbers
     */
    public String getIncorrectRegions(){
        String regions = "";
        for(int i = 0; i < incorrect.size(); i++){
            regions += incorrect.get(i).getNumber() + ((i == incorrect.size() - 1) ? "" : ", ");
        }
        return regions;
    }

    /**
     * The main method. Instantiates a KenKen object then validates the puzzle.
     * @param args Program arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException{
        if(args.length != 1){
            System.err.println("Usage: java KenKen filename");
            System.exit(-1);
        }
        KenKen kk = new KenKen(args[0]);
        kk.check();
    }
}
