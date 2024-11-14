/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.util.Comparator;

/**
 *
 * @author eliot
 */
public class IntCmp implements Comparator<Integer> {

    @Override
    public int compare(Integer a, Integer b) {
        return Integer.compare(a, b); // Ascending order
        // return Integer.compare(b, a); // Descending order
    }
}
