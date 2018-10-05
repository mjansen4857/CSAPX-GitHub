package regions;

/**
 * Implementation of a region for the multiplication operator
 *
 * @author Michael Jansen
 */
public class MultiplyRegion extends Region {
    public MultiplyRegion(int number, int target){
        super(number, target);
    }

    @Override
    public boolean verify() {
        int product = 1;
        for(int value:values){
            product *= value;
        }
        return product == target;
    }

    @Override
    public String toString() {
        return "Region " + number + ": {target: " + target + ", values: " + getValuesString() + " op: *, " + verify() + "}";
    }
}
