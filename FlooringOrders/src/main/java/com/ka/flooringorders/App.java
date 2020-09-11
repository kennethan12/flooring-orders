/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders;

import com.ka.flooringorders.controller.FlooringOrdersController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author kennethan
 */
public class App {

    public static void main(String[] args) {
        // Run by Spring dependency injection.
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringOrdersController controller = ctx.getBean("controller", FlooringOrdersController.class);
        controller.run();
    }
}
