package regions;

import java.util.Collections;

/**
 * Implementation of a region for the division operator
 *
 * @author Michael Jansen
 */
public class DivideRegion extends Region {
    public DivideRegion(int number, int target){
        super(number, target);
    }

    @Override
    public boolean verify() {
        int max = Collections.max(values);
        int quotient = max;
        for(int value:values){
            if(value != max){
                quotient /= value;
            }
        }
        return quotient == target;
    }

    @Override
    public String toString() {
        return "Region " + number + ": {target: " + target + ", values: " + getValuesString() + " op: /, " + verify() + "}";
    }
}
