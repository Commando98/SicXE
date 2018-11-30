package com.company;


import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {


        File file = new File("/home/x/Desktop/TestXE.txt");
        Converter.initialize();
        System.out.println("Pass 1:\n-------\n");
        PassOne p1 = new PassOne(file);
        p1.split();
        p1.locate();
        p1.viewSymbolTable();
        PassTwo p2 = new PassTwo(p1);
        System.out.println("Pass 2:\n-------\n");
        p2.GenerateOPC();
        System.out.println("\n-----------------------------------------------------------\n");
        System.out.println("HTE Record:\n-----------\n");
        p2.HTE();
    }
}
