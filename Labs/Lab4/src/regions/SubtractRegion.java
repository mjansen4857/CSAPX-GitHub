package regions;

import java.util.Collections;

/**
 * Implementation of a region for the subtraction operator
 *
 * @author Michael Jansen
 */
public class SubtractRegion extends Region {
    public SubtractRegion(int number, int target){
        super(number, target);
    }

    @Override
    public boolean verify() {
        int max = Collections.max(values);
        int difference = max;
        for(int value:values){
            if(value != max){
                difference -= value;
            }
        }
        return difference == target;
    }

    @Override
    public String toString() {
        return "Region " + number + ": {target: " + target + ", values: " + getValuesString() + " op: -, " + verify() + "}";
    }
}
