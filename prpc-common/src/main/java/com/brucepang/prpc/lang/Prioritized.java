package com.brucepang.prpc.lang;

import java.util.Comparator;

import static java.lang.Integer.compare;

/**
 * @description: An interface for comparing priorities for sort
 * @author BrucePang
 */
public interface Prioritized extends Comparable<Prioritized>{
    /**
     * The maximum priority
     */
    int MAX_PRIORITY = Integer.MIN_VALUE;

    /**
     * The minimum priority
     */
    int MIN_PRIORITY = Integer.MAX_VALUE;

    /**
     * Normal Priority
     */
    int NORMAL_PRIORITY = 0;

    /**
     * The {@link Comparator} of {@link Prioritized}
     */
    Comparator<Object> COMPARATOR = (arg1, arg2) -> {
        boolean b1 = arg1 instanceof Prioritized;
        boolean b2 = arg2 instanceof Prioritized;
        if (b1 && !b2) {        // arg1 is Prioritized, arg2 is not
            return -1;
        } else if (b2 && !b1) { // arg2 is Prioritized, arg1 is not
            return 1;
        } else if (b1 && b2) {  //  arg1 and arg2 both are Prioritized
            return ((Prioritized) arg1).compareTo((Prioritized) arg2);
        } else {                // no different
            return 0;
        }
    };

    /**
     * Get the priority
     * @return the default is {@link #NORMAL_PRIORITY}
     */
    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    /**
     * Compare the priority of two objects for sorting them:
     *  if return value is less than 0, then the first object has a higher priority
     *  if return value is greater than 0, then the second object has a higher priority
     * @param that the object to compare with
     * @return the result of the comparison
     */
    @Override
    default int compareTo(Prioritized that) {
        return compare(this.getPriority(), that.getPriority());
    }
}
