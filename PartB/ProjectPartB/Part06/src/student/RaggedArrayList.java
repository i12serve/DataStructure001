/**
 * File: RaggedArrayList.java
 * ****************************************************************************
 *                           Revision History
 * ****************************************************************************
 *
 * 8/2015 - Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 */
package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 *
 * @author Bob Booth
 * @param <E> A generic data type so that this structure can be built with any
 * data type (object)
 */
public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces
    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }


    /**
     * ***********************************************************
     * nested class for 2nd level arrays read and understand it. (DONE - do not
     * change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    // ***********************************************************
    public boolean add(E newElement) {
        this.size++;
        ListLoc addLocation = findEnd(newElement);
        L2Array addArray = (L2Array) (l1Array[addLocation.l1]);
//        System.out.println(addLocation.level2Index + " " + addArray.numUsed + " " + addLocation.level1Index);
        if (addLocation.l2 <= addArray.numUsed) {
            System.arraycopy(addArray.items, addLocation.l2, addArray.items, addLocation.l2 + 1, addArray.numUsed - addLocation.l2);
        }
        addArray.items[addLocation.l2] = newElement;
        addArray.numUsed++;

        // If the L2Array is full
        if (addArray.numUsed == addArray.items.length) {

            // Double length of l2array if l2array < l1array
            if (addArray.numUsed < l1Array.length) {
                L2Array newL2Array = new L2Array(addArray.items.length * 2);
                System.arraycopy(addArray.items, 0, newL2Array.items, 0, addArray.numUsed);
                newL2Array.numUsed = addArray.numUsed;
                l1Array[addLocation.l1] = newL2Array;
            } else {
                L2Array newL2Array = new L2Array(addArray.items.length);
                int splitIndex = addArray.items.length / 2;
                System.arraycopy(addArray.items, splitIndex, newL2Array.items, 0, addArray.items.length - splitIndex);
                Arrays.fill(addArray.items, splitIndex, addArray.items.length, null);
                addArray.numUsed = splitIndex;
                newL2Array.numUsed = newL2Array.items.length - splitIndex;
                addArray.numUsed = splitIndex;
                if (l1Array[l1Array.length - 2] != null) {
                    l1Array = Arrays.copyOf(l1Array, l1Array.length * 2);
                }
                System.arraycopy(l1Array, addLocation.l1 + 1, l1Array, addLocation.l1 + 2, l1Array.length - 2 - addLocation.l1);
                l1Array[addLocation.l1 + 1] = newL2Array;
                l1NumUsed++;
            }
        }

        return true;
    }

    /**
     * total size (number of entries) in the entire data structure (DONE - do
     * not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: level 1
     * index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int l1;

        /**
         * Level 2 index
         */
        public int l2;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param l1 input value for property
         * @param l2 input value for property
         */
        public ListLoc(int l1, int l2) {
            this.l1 = l1;
            this.l2 = l2;
        }

        /**
         * test if two ListLoc's are to the same location (done -- do not
         * change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return l1 == other.l1
                    && l2 == other.l2;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext(RaggedArrayList<E> ral) {
            L2Array currentArray = (L2Array) (ral.l1Array[l1]);

            l2++; // Move to next position

            // If we're at the end of current L2 array, find next non-empty L2 array
            if (l2 >= currentArray.numUsed) {
                l1++;
                l2 = 0;
            }
        }
    }

    /**
     * /**
     * Finds the location of the specified item in the Ragged ArrayList. If the
     * item exists, returns the location of the first occurrence. If the item
     * does not exist, returns the location where it should be inserted.
     *
     * @param item The item to search for.
     * @return A ListLoc representing the item's location or the insertion
     * point.
     */
    public ListLoc findFront(E item) {
        int l1 = 0;
        if (l1NumUsed == 0 || ((L2Array) l1Array[l1]).numUsed == 0) {
            return new ListLoc(0, 0);
        }
        // First loop: Find the L2Array that may contain the element
        while (l1 < l1NumUsed && l1Array[l1] != null && comp.compare(((L2Array) l1Array[l1]).items[((L2Array) l1Array[l1]).numUsed - 1], item) < 0) {
            l1++;
        }
        if (l1Array[l1] == null) {
            l1--;
        }
        // If at an empty or larger L1 array, item should go in the first position
        if (l1 == l1NumUsed || comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0) {
            return new ListLoc(l1, 0); // Insert at the start of this L2Array
        }

        // Second loop: Traverse through the L2Array to find the exact position
        L2Array l2Array = (L2Array) l1Array[l1];
        int l2 = 0;
        while (l2 < l2Array.numUsed && comp.compare(l2Array.items[l2], item) < 0) {
            l2++;
        }

        return new ListLoc(l1, l2); // Return the location of the item
    }

    public ListLoc findEnd(E item) {
        int l1 = l1NumUsed - 1;
        if (l1 == -1 || ((L2Array) l1Array[l1]).numUsed == 0) {
            return new ListLoc(0, 0);
        }

        // First loop: Find the L2Array that may contain the element
        while (l1 > 0 && l1Array[l1] != null && comp.compare(((L2Array) (l1Array[l1])).items[0], item) > 0) {
            l1--;
        }
//    if (l1Array[l1] == null) {l1--; }
        // If at an empty or larger L1 array, item should go in the first position
        if (l1 == l1NumUsed || comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0) {
            return new ListLoc(l1, 0); // Insert at the start of this L2Array
        }

        // Second loop: Traverse through the L2Array backwards to find the exact position
        L2Array l2Array = (L2Array) l1Array[l1];
        int l2 = l2Array.numUsed - 1;
        while (l2 >= 0 && comp.compare(l2Array.items[l2], item) > 0) {
            l2--;
        }
        l2++;

        return new ListLoc(l1, l2); // Return the location of the item

    }

    /**
     * siginal list is unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        RaggedArrayList<E> subList = new RaggedArrayList<E>(comp); // New empty list

        // Find the start and end positions
        ListLoc startLoc = findFront(fromElement);
        ListLoc endLoc = findFront(toElement);

        // Traverse from startLoc to endLoc and add elements to subList
        ListLoc currentLoc = new ListLoc(startLoc.l1, startLoc.l2);
        while (!currentLoc.equals(endLoc)) {
            L2Array l2Array = (L2Array) l1Array[currentLoc.l1];
            subList.add(l2Array.items[currentLoc.l2]); // Add the element to subList
            currentLoc.moveToNext(this); // Move to the next element
        }

        return subList; // Return the new sublist
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    public boolean contains(E item) {

        ListLoc loc = findFront(item); // Get the location using findFront
        int l1 = loc.l1; // The L1 index
        int l2 = loc.l2; // The L2 index

        // Check if the location is within bounds and if the item matches
        if (l1 < l1NumUsed) {
            L2Array l2Array = (L2Array) l1Array[l1];
            if (l2 < l2Array.numUsed && comp.compare(l2Array.items[l2], item) == 0) {
                return true; // Item is found at the locaFtion
            }
        }

        return false; // Item is not found
    }

    public void toArray(E[] a) {
        if (a.length != size) {
            throw new IllegalArgumentException("Array size must match list size.");
        }
        if (size == 0)
        {
            return;
        }
        int index = 0;
        for (E item : this) { // Using the iterator to traverse the list
            a[index] = item;
            index++;
        }

    }

    
    public void debugPrintAll() {
        int count = 0;
        for (int i = 0; i < l1NumUsed; i++) {
            L2Array currentL2 = (L2Array) l1Array[i];
            for (int j = 0; j < currentL2.numUsed; j++) {
                count++;
                System.out.printf("%s ", currentL2.items[j].toString());
            }
            System.out.println("");
        }
        System.out.printf("Total Number of Elements: %d\n", count);
        System.out.printf("size Variable: %d\n", this.size());
    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc current;

        public Itr() {
            current = new ListLoc(0, 0);
        }

        @Override
        public boolean hasNext() {
            // If we're beyond the last L1 array, no more elements
            if (current.l1 >= l1NumUsed) {
                return false;
            }

            L2Array currentArray = (L2Array) l1Array[current.l1];

            // If we're within the current L2 array's used elements, we have more
            if (current.l2 < currentArray.numUsed) {
                return true;
            }

            // If we're at the end of current L2 array, check if there are more L1 arrays
            int nextL1 = current.l1 + 1;
            while (nextL1 < l1NumUsed) {
                L2Array nextArray = (L2Array) l1Array[nextL1];
                if (nextArray != null && nextArray.numUsed > 0) {
                    return true;
                }
                nextL1++;
            }

            return false;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            L2Array l2Array = (L2Array) l1Array[current.l1];
            E item = l2Array.items[current.l2];
            current.moveToNext(RaggedArrayList.this);
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported.");
        }
    }
    /**
     * Written by Bob Booth as part of the testing harness for Project Part 5,
     * this stats method allows any RaggedArrayList object to print its own
     * statistics. You must remember to reset the Comparator object's count
     * field before creating a ragged array list. Modified to be a RAL method by
     * Anne Applin
     */
    public void stats() {
        System.out.println("STATS:");
        int size = this.size();
        System.out.println("list size N = " + size);

        // level 1 array
        int l1NumUsed = this.l1NumUsed;
        System.out.println("level 1 array " + l1NumUsed + " of "
                + this.l1Array.length + " used.");

        // level 2 arrays
        int minL2size = Integer.MAX_VALUE, maxL2size = 0;
        for (int i1 = 0; i1 < this.l1NumUsed; i1++) {
            RaggedArrayList<Song>.L2Array l2array
                    = (RaggedArrayList<Song>.L2Array) (this.l1Array[i1]);
            minL2size = Math.min(minL2size, l2array.numUsed);
            maxL2size = Math.max(maxL2size, l2array.numUsed);
        }
        System.out.printf("level 2 array sizes: min = %d used, avg = %.1f "
                + "used, max = %d used.%n%n",
                minL2size,
                (double) size / l1NumUsed, maxL2size);
    }
}
