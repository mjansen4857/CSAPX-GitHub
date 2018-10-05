package regions;

/**
 * Implementation of a region for the addition operator
 *
 * @author Michael Jansen
 */
public class AddRegion extends Region {
    public AddRegion(int number, int target){
        super(number, target);
    }

    @Override
    public boolean verify() {
        int sum = 0;
        for(int value:values){
            sum += value;
        }
        return sum == target;
    }

    @Override
    public String toString() {
        return "Region " + number + ": {target: " + target + ", values: " + getValuesString() + " op: +, " + verify() + "}";
    }
}
