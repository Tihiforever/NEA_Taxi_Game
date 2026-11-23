package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class UpgradeScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;
    private final PlayerStats playerStats;
    private final Taxi taxi;
    private final UpgradeManager upgradeManager;
    private final InputHandeler inputHandeler = new InputHandeler();

    private final Upgrades[][] carUpgrades = {{Upgrades.SPEED1, Upgrades.SPEED2, Upgrades.SPEED3}, {Upgrades.HANDLING1, Upgrades.HANDLING2, Upgrades.HANDLING3}, {Upgrades.BRAKES1, Upgrades.BRAKES2}};
    private final Upgrades[][] businessUpgrades = {{Upgrades.FARES1, Upgrades.FARES2, Upgrades.FARES3}, {Upgrades.PASSENGERS1, Upgrades.PASSENGERS2, Upgrades.PASSENGERS3}, {Upgrades.DISPATCH1, Upgrades.DISPATCH2}};

    private final String[] carNames = {"SPEED", "HANDLING", "BRAKES"};
    private final String[] businessNames = {"FARES", "PASSENGERS", "DISPATCH"};

    private final String[][] carUpgradeNames = {{"Engine Tuning", "Turbocharger", "Racing ECU"}, {"Premium Tyres", "Sports Suspension", "Advanced Steering"}, {"Performance Brakes", "Reinforced Chassis"}};
    private final String[][] carEffects = {{"+10% maximum speed", "+15% acceleration", "+15% maximum speed"}, {"Improved grip and handling", "+15% turning responsiveness", "Improved cornering"}, {"Improved braking", "40% less collision penalties"}};

    private final String[][] businessUpgradeNames = {{"Premium Fare Meter", "VIP Service", "Executive Taxi"}, {"Extended Seating", "Passenger Comfort", "Premium Cab"}, {"Improved Radio", "Advanced Dispatch"}};
    private final String[][] businessEffects = {{"+10% job fares", "+10% job fares", "+15% job fares"}, {"Accept another active job", "Slower patience loss", "Increased completion rewards"}, {"+5 available jobs", "30% long-distance job chance"}};

    private final float nodeWidth = 200f;
    private final float nodeHeight = 90f;
    private final float nodeGap = 20f;

    public UpgradeScreen(ScreenManager screenManager, Main main, PlayerStats playerStats, Taxi taxi, UpgradeManager upgradeManager) {
        this.screenManager = screenManager;
        this.main = main;
        this.playerStats = playerStats;
        this.taxi = taxi;
        this.upgradeManager = upgradeManager;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandeler);
    }

    @Override
    public void render(float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        Gdx.gl.glClearColor(0.12f, 0.14f, 0.20f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.getBatch().getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        main.getSr().setProjectionMatrix(main.getBatch().getProjectionMatrix());

        float mouseX = inputHandeler.getMouseX();
        float mouseY = height - inputHandeler.getMouseY();

        float startX = (width - 1320f) / 2f;

        ShapeRenderer sr = main.getSr();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        for (int side = 0; side < 2; side++) {
            Upgrades[][] tree;

            if (side == 0) tree = carUpgrades;
            else tree = businessUpgrades;

            for (int column = 0; column < tree.length; column++) {
                float x = startX + side * 680f + column * (nodeWidth + nodeGap);

                for (int tier = 0; tier < tree[column].length; tier++) {
                    Upgrades upgrade = tree[column][tier];
                    float y = height - 300f - tier * (nodeHeight + nodeGap);

                    if (upgradeManager.hasUpgrade(upgrade)) sr.setColor(Color.GREEN);
                    else if (!canBuy(tree[column], tier)) sr.setColor(Color.DARK_GRAY);
                    else if (playerStats.getMoney() >= upgrade.price) sr.setColor(Color.SKY);
                    else sr.setColor(Color.FIREBRICK);

                    sr.rect(x, y, nodeWidth, nodeHeight);
                }
            }
        }

        sr.end();

        main.getBatch().begin();

        main.getFont().setColor(Color.WHITE);

        main.getFont().getData().setScale(4f);
        main.getFont().draw(main.getBatch(), "UPGRADES", width / 2f - 160f, height - 55f);

        main.getFont().getData().setScale(1.5f);
        main.getFont().draw(main.getBatch(), "MONEY: £" + String.format("%.0f", playerStats.getMoney()), width - 250f, height - 45f);

        main.getFont().getData().setScale(2f);
        main.getFont().draw(main.getBatch(), "CAR", width / 4f - 35f, height - 125f);
        main.getFont().draw(main.getBatch(), "BUSINESS", width * 0.75f - 75f, height - 125f);

        for (int side = 0; side < 2; side++) {

            Upgrades[][] tree;
            String[] branches;
            String[][] names;
            String[][] effects;

            if (side == 0) {
                tree = carUpgrades;
                branches = carNames;
                names = carUpgradeNames;
                effects = carEffects;
            } else {
                tree = businessUpgrades;
                branches = businessNames;
                names = businessUpgradeNames;
                effects = businessEffects;
            }

            for (int column = 0; column < tree.length; column++) {
                float x = startX + side * 680f + column * (nodeWidth + nodeGap);

                main.getFont().getData().setScale(1f);
                main.getFont().draw(main.getBatch(), branches[column], x, height - 180f);

                for (int tier = 0; tier < tree[column].length; tier++) {
                    Upgrades upgrade = tree[column][tier];
                    float y = height - 300f - tier * (nodeHeight + nodeGap);

                    String status;

                    if (upgradeManager.hasUpgrade(upgrade)) status = "OWNED";
                    else if (!canBuy(tree[column], tier)) status = "LOCKED";
                    else status = "£" + upgrade.price;

                    main.getFont().draw(main.getBatch(), names[column][tier], x + 8f, y + 72f);
                    main.getFont().draw(main.getBatch(), effects[column][tier], x + 8f, y + 43f);
                    main.getFont().draw(main.getBatch(), status, x + 8f, y + 15f);
                }
            }
        }

        main.getFont().getData().setScale(1f);
        main.getBatch().end();

        if (inputHandeler.getLastKeyPressed() == Input.Keys.ENTER) {
            inputHandeler.clearLastKeyPressed();
            screenManager.setScreen(Screen_Type.GAME_SCREEN);
            return;
        }

        if (inputHandeler.isMouseClicked()) {
            for (int side = 0; side < 2; side++) {
                Upgrades[][] tree;

                if (side == 0) tree = carUpgrades;
                else tree = businessUpgrades;

                for (int column = 0; column < tree.length; column++) {
                    float x = startX + side * 680f + column * (nodeWidth + nodeGap);

                    for (int tier = 0; tier < tree[column].length; tier++) {
                        float y = height - 300f - tier * (nodeHeight + nodeGap);

                        if (mouseX >= x && mouseX <= x + nodeWidth &&
                            mouseY >= y && mouseY <= y + nodeHeight) {

                            if (canBuy(tree[column], tier)) upgradeManager.buyUpgrade(tree[column][tier], playerStats, taxi);
                        }
                    }
                }
            }
            inputHandeler.resetMouseClick();
        }
    }

    private boolean canBuy(Upgrades[] branch, int tier) {
        if (tier == 0) return true;
        return upgradeManager.hasUpgrade(branch[tier - 1]);
    }
}
