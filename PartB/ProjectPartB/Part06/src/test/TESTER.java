/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.util.Comparator;
import student.RaggedArrayList;
import java.util.Comparator;
import student.RaggedArrayList;


/**
 *
 * @author eliot
 */
public class TESTER {
    public static void main(String[] args) {
        IntCmp cmp = new IntCmp();
        RaggedArrayList<Integer> ral;
        ral = new RaggedArrayList<Integer>(cmp);
        for (int i = 0; i < 20; i+=2) {
            ral.add(i);
            System.out.println("");
        }
        ral.debugPrintAll();
        for (int i = 0; i < 20; i+=2) {
            ral.add(i + 1);
            System.out.println("");
        }
        ral.debugPrintAll();

    }
}
