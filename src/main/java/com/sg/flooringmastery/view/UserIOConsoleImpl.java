/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Jeff
 */
public class UserIOConsoleImpl implements UserIO {

    @Override
    public void print(String message) {
        System.out.print(message + "\n");
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min) {
        boolean inputValid = false;
        BigDecimal bd; 
        String userInput;
        do {
            print(prompt);
            Scanner in = new Scanner(System.in);
            userInput = in.nextLine();
            try {
                bd = new BigDecimal(userInput);
                if (bd.compareTo(BigDecimal.ZERO) != 1) {
                    print("Input must be greater than 0. Please try again. \n");
                } else {
                    inputValid = true;
                }
            } catch (NumberFormatException e) {
                print("Input must be a number. Please try again. \n");
            }

        } while (!inputValid);
        BigDecimal inputAsBD = new BigDecimal(userInput);
        return inputAsBD;
    }

    @Override
    public LocalDate readDate(String prompt) {
        print(prompt);
        Scanner in = new Scanner(System.in);
        boolean inputValid = false;
        String userInput;
        LocalDate inputAsLD = null;
        do {
            userInput = in.nextLine();
            if (userInput.length() != 10 || userInput.charAt(2) != '/' || 
                    userInput.charAt(5) != '/') {
                inputValid = false;
                print("Invalid date input. Please try again, using \"mm/dd/yyyy\" format.");
            } else {
                try {
                    inputAsLD = LocalDate.parse(userInput, 
                            DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    inputValid = true;
                } catch (DateTimeParseException e) {
                    print("Invalid date input. " + userInput + " is not a valid "
                            + "date. Please try again.");
                }
            }
        } while(!inputValid);
        return inputAsLD;
    }

    @Override
    public String readString(String prompt) {
        print(prompt);
        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();
        return userInput;
    }

    @Override
    public int readInt(String prompt) {
        int userInput = 0;
        boolean inputValid = false;
        Scanner in = new Scanner(System.in);
        do {
            print(prompt);
            try {
                userInput = in.nextInt();
                inputValid = true;
            } catch (InputMismatchException e) {
                print("Invalid input. Please try again. \n");
                in.nextLine();
            }  
        } while (!inputValid);
        return userInput;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int userInput = 0;
        boolean inputValid = false;
        do {
            userInput = readInt(prompt);
            if (userInput < min || userInput > max) {
                print("Input provided not within range. Please try again. \n");
            } else {
                inputValid = true;
            }
        } while (!inputValid);
        return userInput;
    }
    
}
