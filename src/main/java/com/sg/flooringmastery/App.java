/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringOrderController;
import java.util.Scanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 *
 * @author Jeff
 */
public class App {
    public static void main(String[] args) {
//        UserIO myIo = new UserIOConsoleImpl();
//        FlooringOrderView myView = new FlooringOrderView(myIo);
//        OrderDao myOrderDao = new OrderDaoFileImpl();
//        ProductDao myProductDao = new ProductDaoFileImpl();
//        TaxRateDao myTaxDao = new TaxRateDaoFileImpl();
//        FlooringOrderServiceLayer myService = new FlooringOrderServiceLayerImpl(myOrderDao, myProductDao, myTaxDao);
//        FlooringOrderController controller = new FlooringOrderController(myView, myService);
//        controller.run();

//        ApplicationContext ctx = new ClassPathXmlApplicationContext("prod-config.xml");

        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("*-config.xml");
        ctx.refresh();
        showMode(ctx);
        String choice = promptForModeChange();
        if (!choice.isEmpty()) {
            ctx = new GenericXmlApplicationContext();
            ctx.getEnvironment().setActiveProfiles("training");
            ctx.load("*-config.xml");
            ctx.refresh();
        }
        showMode(ctx);
        
        FlooringOrderController controller = ctx.getBean("controller", FlooringOrderController.class);
        controller.run();
    }

    private static void showMode(ApplicationContext ctx) {
        String modeString = ctx.getBean("profile", String.class);
        System.out.println("Current mode: " + modeString);
    }

    private static String promptForModeChange() {
        Scanner in = new Scanner(System.in);
        String choice = "";
        do {
            System.out.println("Please type TRAINING to switch to Training mode, "
                    + "or enter to proceed in the default PROD mode.");
            choice = in.nextLine();
            if (!(choice.isEmpty() || choice.equalsIgnoreCase("TRAINING"))) {
                System.out.println("Invalid selection. Please try again.");
            }
        } while (!(choice.isEmpty() || choice.equalsIgnoreCase("TRAINING")));
        return choice;
    }
}
