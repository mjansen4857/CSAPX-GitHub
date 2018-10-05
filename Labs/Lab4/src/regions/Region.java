package regions;

import java.util.ArrayList;

/**
 * This class stores the data for a region.
 *
 * @author Michael Jansen
 */
public abstract class Region {
    protected int number;
    protected int target;
    protected ArrayList<Integer> values;

    /**
     * Constructs a region.
     * @param number The region number
     * @param target The target value of the region
     */
    public Region(int number, int target){
        this.number = number;
        this.target = target;
        this.values = new ArrayList<>();
    }

    /**
     * Add a value to the region
     * @param value The value to add
     */
    public void addValue(int value){
        this.values.add(value);
    }

    /**
     * Gets the region number
     * @return The region number
     */
    public int getNumber(){
        return number;
    }

    /**
     * Creates a String of all values contained in the region separated by a comma
     * @return The comma-separated values
     */
    public String getValuesString(){
        String str = "";
        for(int i = 0; i < values.size(); i++){
            str += values.get(i) + ((i == values.size() - 1) ? "" : ", ");
        }
        return str;
    }

    /**
     * Verifies whether a region is correct or not. Implementation changes based on the
     * operator of the region
     * @return Is the region correct
     */
    public abstract boolean verify();
}
