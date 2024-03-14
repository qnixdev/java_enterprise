package com.java_enterprise.home_work_4;

public class Client implements Runnable {
    private final Bar bar;
    private final String clientName;

    public Client(
        Bar bar,
        String clientName
    ) {
        this.bar = bar;
        this.clientName = clientName;
    }

    @Override
    public void run() {
        bar.createOrder(this.clientName, Drink.getRandomDrink());
    }
}