package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter {
    private ShapeRenderer sr;

    // Main game objects
    /*private City city;
    private Taxi taxi;
    private Graph graph;
    private NPCManager npcManager;
    */

    //pathfinding
    //private Pathfinder pathfinder;

    // Cameras for the game world and HUD
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    // Used to draw text on screen
    private SpriteBatch batch;
    private BitmapFont font;

    // Flags/Modes and Nodes
    private boolean debugMode;
    private boolean mapMode;
    //private Node selectedNode;
    //private Node end;

    //screen stuff
    private final ScreenManager screenManager;
    private final Main main;

    public GameScreen(ScreenManager screenManager, Main main){
        this.screenManager = screenManager;
        this.main = main;
    }

    @Override
    public void show(){
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        /*city = new City();
        taxi = new Taxi(city.getWorldWidth() / 2f + 280, city.getWorldHeight() / 2f);
        graph = new Graph(city);
        pathfinder = new Pathfinder(graph);
        npcManager = new NPCManager(graph, pathfinder, 200);
`       */

        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        /*
        endNode = graph.getNodes().get(0);
         */
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        updateEntities(delta);
        updateCamera(delta);

        drawWorld();
        drawHud(delta);
    }

    @Override
    public void dispose(){

    }

    private void handleInput(){
        //main game button inputs
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) show();  //re-create map
        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)) mapMode = !mapMode; //mapmode changer
        //if(Gdx.input.isKeyJustPressed(Input.Keys.P)) end = graph.getNodes().get((int)(Math.random() * graph.getNodes().size())); //pick a random bode

        //Screen change
        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) screenManager.setScreen(Screen_Type.GAME_OVER);
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) screenManager.setScreen(Screen_Type.JOB_SCREEN);

        // Node selection in mapmode
//        if (mapMode && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//            Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
//            selectedNode = graph.getClosestNode(mousePos.x, mousePos.y);
//        }
    }

    private void updateEntities(float delta){
        //taxi.update();
        //npcManager.update(delta);
    }

    private void updateCamera(float delta){
        if(!mapMode) {
            // Smoothly follow the taxi with the camera
            //camera.position.x += (taxi.getX() + taxi.getWidth() / 2f - camera.position.x) * 0.12f;
            //camera.position.y += (taxi.getY() + taxi.getHeight() / 2f - camera.position.y) * 0.12f;
        } else {
            float speed = 1500*Gdx.graphics.getDeltaTime();

            if(Gdx.input.isKeyPressed(Input.Keys.T)) camera.position.y += speed;
            if(Gdx.input.isKeyPressed(Input.Keys.G)) camera.position.y -= speed;
            if(Gdx.input.isKeyPressed(Input.Keys.F)) camera.position.x -= speed;
            if(Gdx.input.isKeyPressed(Input.Keys.H)) camera.position.x += speed;
        }
        camera.update();
    }

    private void drawWorld(){
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Environment & Entities
//        city.draw(sr);
//        taxi.draw(sr);
//        graph.draw(sr);
//        npcManager.draw(sr);

        sr.end();
    }

    private void drawHud(float delta){
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();
        font.getData().scale(1);

        // Display the taxi's current speed
        font.draw(batch, "Taxi-Game", 20, Gdx.graphics.getHeight() - 20);

        batch.end();

    }



}
