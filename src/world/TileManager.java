package world;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[33];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/assets/maps/map0.txt");
    }

    public void getTileImage() {

        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/0.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/1.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/2.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/3.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/4.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/5.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/6.png"));

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/7.png"));

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/8.png"));

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/9.png"));

            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/10.png"));

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/11.png"));

            tile[12] = new Tile();
            tile[12].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/12.png"));

            tile[13] = new Tile();
            tile[13].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/13.png"));

            tile[14] = new Tile();
            tile[14].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/14.png"));

            tile[15] = new Tile();
            tile[15].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/15.png"));

            tile[16] = new Tile();
            tile[16].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/16.png"));

            tile[17] = new Tile();
            tile[17].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/17.png"));

            tile[18] = new Tile();
            tile[18].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/18.png"));

            tile[19] = new Tile();
            tile[19].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/19.png"));

            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/20.png"));

            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/21.png"));

            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/22.png"));

            tile[23] = new Tile();
            tile[23].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/23.png"));

            tile[24] = new Tile();
            tile[24].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/24.png"));

            tile[25] = new Tile();
            tile[25].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/25.png"));

            tile[26] = new Tile();
            tile[26].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/26.png"));

            tile[27] = new Tile();
            tile[27].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/27.png"));

            tile[28] = new Tile();
            tile[28].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/28.png"));

            tile[29] = new Tile();
            tile[29].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/29.png"));

            tile[30] = new Tile();
            tile[30].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/30.png"));

            tile[31] = new Tile();
            tile[31].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/31.png"));

            tile[32] = new Tile();
            tile[32].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/tiles/map0/32.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMap(String filePath) {

        try {

            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader (new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine();

                while (col < gp.maxWorldCol) {

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;

                }

                if (col == gp.maxWorldCol) {

                    col = 0;
                    row++;

                }

            }

            br.close();

        } catch (Exception e) {



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

            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }

        }

    }
    
}
