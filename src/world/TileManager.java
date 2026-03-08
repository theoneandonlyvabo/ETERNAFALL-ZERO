package world;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.GameTool;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[33];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/map0.txt");
    }

    public void getTileImage() {
        setup(0,  true);
        setup(1,  false);
        setup(2,  false);
        setup(3,  true);
        setup(4,  false);
        setup(5,  true);
        setup(6,  true);
        setup(7,  true);
        setup(8,  false);
        setup(9,  true);
        setup(10, false);
        setup(11, true);
        setup(12, true);
        setup(13, true);
        setup(14, true);
        setup(15, true);
        setup(16, true);
        setup(17, true);
        setup(18, true);
        setup(19, true);
        setup(20, true);
        setup(21, false);
        setup(22, true);
        setup(23, false);
        setup(24, true);
        setup(25, true);
        setup(26, true);
        setup(27, true);
        setup(28, true);
        setup(29, true);
        setup(30, true);
        setup(31, true);
        setup(32, true);
    }

    public void setup(int index, boolean collision) {
        GameTool gTool = new GameTool();
        try {
            InputStream is = getClass().getResourceAsStream("/tiles/map0/" + index + ".png");
            if (is == null) {
                System.err.println("Tile not found: /tiles/map0/" + index + ".png");
                return;
            }
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(is);
            tile[index].image = gTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String resourcePath) {
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Map not found: " + resourcePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.split(",");
                while (col < gp.maxWorldCol) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col].trim());
                    col++;
                }

                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                if (tile[tileNum] != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                }
            }

            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}