package io.github.tihiforever;

public enum Upgrades {

    SPEED1(500),
    SPEED2(1000),
    SPEED3(2000),

    HANDLING1(450),
    HANDLING2(900),
    HANDLING3(1800),

    BRAKES1(400),
    BRAKES2(1200),

    FARES1(750),
    FARES2(1500),
    FARES3(3000),

    PASSENGERS1(1200),
    PASSENGERS2(1200),
    PASSENGERS3(2500),

    DISPATCH1(500),
    DISPATCH2(1500);

    public final int price;

    Upgrades(int price) {
        this.price = price;
    }
}
