/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.sprite.*;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private List<Monster> monsterList;
    public List<Bomb> bombList = new LinkedList<>();
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private boolean explosionCreated = false;

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        this.monsterList = game.getMonsterList();
        this.bombList = game.getBombList();

        initialize(stage, game);
        buildAndSetGameLoop();
    }

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();

        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        //Create decor sprites
        game.getWorld().forEach((pos, d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);

        //Create monster sprites
        for (Monster monster : monsterList) {
            sprites.add(SpriteFactory.createMonster(layer, monster));
        }
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();

                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if (input.isBomb()) {
            if (player.getNumBomb() > 0) {
                Bomb bomb = new Bomb(game, player.getPosition(), now);
                bombList.add(bomb);
                sprites.add(SpriteFactory.createBomb(layer, bomb));
                player.bombNumDec();
            } else {
                System.out.println("No more bombs...");
            }
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    private void update(long now) {
        player.update(now);

        for( Monster monster : monsterList ) {
            monster.update(now);
        }

        if (!bombList.isEmpty()) {
            Iterator<Bomb> bombIterator = bombList.iterator();
            while(bombIterator.hasNext()) {
                Bomb bomb = bombIterator.next();
                bomb.update(now);

                if (bomb.destroyed[0] && !bomb.explosionCreated) {
                    displayExplosionSprites(bomb);
                    bomb.explosionCreated = true;
                }

                if (bomb.destroyed[1]) {
                    game.getWorld().needDecorRefresh = true;

                    clearSpritesAfterExplosion(bomb);
                    bombIterator.remove();
                }
            }
        }

        if (game.getWorld().needDecorRefresh) {
            System.out.println("Updating sprites...");
            refreshDecorSprites();
        }

        Iterator<Monster> monsterIterator = monsterList.iterator();
        while(monsterIterator.hasNext()) {
            Monster monster = monsterIterator.next();
            if(!monster.isAlive()){
                clearMonsterSprite(monster);
                monsterIterator.remove();
            }
        }

        if (!player.isAlive()) {
            gameLoop.stop();
            showMessage("YOU LOOSE!", Color.RED);
        }

        if(player.isGod()){
            spritePlayer.changeColor(true);
        }
        else{
            spritePlayer.changeColor(false);
        }

        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("YOU WIN!", Color.BLUE);
        }
    }


    public void displayExplosionSprites(Bomb bomb) {
        displayExplosionSprites(Direction.N, bomb);
        displayExplosionSprites(Direction.S, bomb);
        displayExplosionSprites(Direction.W, bomb);
        displayExplosionSprites(Direction.E, bomb);
    }

    public void displayExplosionSprites(Direction dir, Bomb bomb){
        int j;
        switch(dir){
            case S:
                j = 1;
                break;
            case W:
                j = 2;
                break;
            case E:
                j = 3;
                break;
            default:
                j = 0;
        }
        int i=1;
        Position nextPos = dir.nextPosition(bomb.getPosition());
        while(i<bomb.spriteRange[j]+1){
            if(game.getWorld().isInside(nextPos)) {
                Decor nextDecor = game.getWorld().get(nextPos);
                if (game.getWorld().isEmpty(nextPos) || nextDecor instanceof Bonus) {
                    Sprite explosion = SpriteFactory.createExplosion(layer, nextPos, bomb);
                    sprites.add(explosion);
                }
                nextPos = dir.nextPosition(nextPos);
            }
            i++;
        }
    }

    public void clearSpritesAfterExplosion(Bomb bomb){
        Iterator<Sprite> spriteIterator = sprites.iterator();
        while(spriteIterator.hasNext()) {
            Sprite sprite = spriteIterator.next();
            if (sprite instanceof SpriteBomb) {
                SpriteBomb spriteBomb = (SpriteBomb) sprite;
                if(spriteBomb.getGameObject() == bomb) {
                    spriteBomb.remove();
                    spriteIterator.remove();
                }
            }
            if(sprite instanceof SpriteExplosion){
                SpriteExplosion spriteExplosion = (SpriteExplosion) sprite;
                if(spriteExplosion.getGameObject() == bomb) {
                    spriteExplosion.remove();
                    spriteIterator.remove();
                }
            }
        }
    }

    public void clearMonsterSprite(Monster monster){
        Iterator<Sprite> spriteIterator = sprites.iterator();
        while(spriteIterator.hasNext()) {
            Sprite sprite = spriteIterator.next();
            if (sprite instanceof SpriteMonster) {
                SpriteMonster spriteMonster = (SpriteMonster) sprite;
                if(spriteMonster.getGameObject() == monster) {
                    spriteMonster.remove();
                    spriteIterator.remove();
                }
            }
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
        // last rendering to have player in the foreground
        spritePlayer.render();
    }

    public void start() {
        gameLoop.start();
    }

    public void removeSpritesOfAKind(String kind){
        Iterator<Sprite> spriteIterator = sprites.iterator();
        while(spriteIterator.hasNext()) {
            Sprite s = spriteIterator.next();
            if (s.toString() == kind) {
                s.remove();
                spriteIterator.remove();
            }
        }
    }

    private void refreshDecorSprites(){ //Re-display every decor sprites (stones, trees, boxes and bonus)
        game.getWorld().needDecorRefresh = false;

        Iterator<Sprite> spriteIterator = sprites.iterator();
        while(spriteIterator.hasNext()) {
            Sprite s = spriteIterator.next();
            if (s.toString() == "SpriteDecor") {
                s.remove();
                spriteIterator.remove();
            }
        }
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
    }

    /**private void refreshMonsterSprites(){ //Re-display monsters (after each modification)
        for(Monster monster: monsterList){
            monster
        }
    }**/

    /**private void refreshAllSprites(){
     game.getWorld().needDecorRefresh = false;
     sprites.forEach(Sprite::remove);
     sprites.clear();
     game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
     refreshBombSprites();
     }**/
}


