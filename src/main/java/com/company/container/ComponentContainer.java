package com.company.container;

import com.company.Car;
import com.company.enums.AdminStatus;
import com.company.enums.CustomerStatus;
import com.company.model.Customer;
import com.company.model.Order;
import com.company.model.Product;

import java.util.HashMap;
import java.util.Map;

public abstract class ComponentContainer {
    public static final String BOT_TOKEN = "your telegram bot token";
    public static final String BOT_NAME = "your telegram link";

    public static final String ADMIN_ID = "...."; //your id

    public static Car MY_TELEGRAM_BOT;

    public static String PATH = "src/main/resources/";




    public static Map<String, com.company.model.Product> productMap = new HashMap<String, com.company.model.Product>();
    public static Map<String, AdminStatus> productStepMap = new HashMap<>();
    public static Map<String, Product> crudStepMap = new HashMap<String, com.company.model.Product>();
    public static Map<String, Customer> customerMap = new HashMap<>();

    public static Map<String, CustomerStatus> customerStepMap = new HashMap<>();
    public static Map<String, Order> orderMap = new HashMap<String, com.company.model.Order>();
}
