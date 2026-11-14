package io.github.tihiforever;

public class UpgradeManager {

    private boolean[] bought = new boolean[Upgrades.values().length];

    public boolean buyUpgrade(Upgrades upgrade, PlayerStats stats, Taxi taxi) {

        int id = upgrade.ordinal();

        if (bought[id]) return false;
        if (stats.getMoney() < upgrade.price) return false;

        if (upgrade == Upgrades.SPEED2 && !bought[0]) return false;
        if (upgrade == Upgrades.SPEED3 && !bought[1]) return false;

        if (upgrade == Upgrades.HANDLING2 && !bought[3]) return false;
        if (upgrade == Upgrades.HANDLING3 && !bought[4]) return false;

        if (upgrade == Upgrades.BRAKES2 && !bought[6]) return false;

        if (upgrade == Upgrades.FARES2 && !bought[8]) return false;
        if (upgrade == Upgrades.FARES3 && !bought[9]) return false;

        if (upgrade == Upgrades.PASSENGERS2 && !bought[11]) return false;
        if (upgrade == Upgrades.PASSENGERS3 && !bought[12]) return false;

        if (upgrade == Upgrades.DISPATCH2 && !bought[14]) return false;

        stats.addMoney(-upgrade.price);
        bought[id] = true;

        if (upgrade == Upgrades.SPEED1) taxi.maxSpeed *= 1.10f;
        if (upgrade == Upgrades.SPEED2) taxi.acceleration *= 1.15f;
        if (upgrade == Upgrades.SPEED3) taxi.maxSpeed *= 1.15f;

        if (upgrade == Upgrades.HANDLING1) taxi.handlingMultiplier *= 1.10f;
        if (upgrade == Upgrades.HANDLING2) taxi.turnSpeed *= 1.15f;
        if (upgrade == Upgrades.HANDLING3) taxi.handlingMultiplier *= 1.15f;

        if (upgrade == Upgrades.BRAKES1) taxi.breakMultiplier *= 1.20f;

        return true;
    }

    public boolean hasUpgrade(Upgrades upgrade) {
        return bought[upgrade.ordinal()];
    }
}
