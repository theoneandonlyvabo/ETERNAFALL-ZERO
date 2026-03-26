package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX   = entity.worldX + entity.hitbox.x;
        int entityRightWorldX  = entity.worldX + entity.hitbox.x + entity.hitbox.width;
        int entityTopWorldY    = entity.worldY + entity.hitbox.y;
        int entityBottomWorldY = entity.worldY + entity.hitbox.y + entity.hitbox.height;

        int entityLeftCol   = entityLeftWorldX   / gp.tileSize;
        int entityRightCol  = entityRightWorldX  / gp.tileSize;
        int entityTopRow    = entityTopWorldY    / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {

            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
                    entity.collisionMade = true;
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
                    entity.collisionMade = true;
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
                    entity.collisionMade = true;
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision)
                    entity.collisionMade = true;
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {

            if (gp.obj[i] != null) {

                entity.hitbox.x    = entity.worldX + entity.hitboxDefaultX;
                entity.hitbox.y    = entity.worldY + entity.hitboxDefaultY;
                gp.obj[i].hitbox.x = gp.obj[i].worldX + gp.obj[i].hitboxDefaultX;
                gp.obj[i].hitbox.y = gp.obj[i].worldY + gp.obj[i].hitboxDefaultY;

                switch (entity.direction) {
                    case "up":    entity.hitbox.y -= entity.speed; break;
                    case "down":  entity.hitbox.y += entity.speed; break;
                    case "left":  entity.hitbox.x -= entity.speed; break;
                    case "right": entity.hitbox.x += entity.speed; break;
                }

                if (entity.hitbox.intersects(gp.obj[i].hitbox)) {
                    if (gp.obj[i].collision) entity.collisionMade = true;
                    if (player) index = i;
                }

                entity.hitbox.x    = entity.hitboxDefaultX;
                entity.hitbox.y    = entity.hitboxDefaultY;
                gp.obj[i].hitbox.x = gp.obj[i].hitboxDefaultX;
                gp.obj[i].hitbox.y = gp.obj[i].hitboxDefaultY;
            }
        }

        return index;
    }

    public int checkEntity(Entity entity, Entity[] targets) {

        int index = 999;

        for (int i = 0; i < targets.length; i++) {

            if (targets[i] != null) {

                entity.hitbox.x     = entity.worldX  + entity.hitboxDefaultX;
                entity.hitbox.y     = entity.worldY  + entity.hitboxDefaultY;
                targets[i].hitbox.x = targets[i].worldX + targets[i].hitboxDefaultX;
                targets[i].hitbox.y = targets[i].worldY + targets[i].hitboxDefaultY;

                switch (entity.direction) {
                    case "up":    entity.hitbox.y -= entity.speed; break;
                    case "down":  entity.hitbox.y += entity.speed; break;
                    case "left":  entity.hitbox.x -= entity.speed; break;
                    case "right": entity.hitbox.x += entity.speed; break;
                }

                if (entity.hitbox.intersects(targets[i].hitbox)) {
                    entity.collisionMade = true;
                    index = i;
                }

                entity.hitbox.x     = entity.hitboxDefaultX;
                entity.hitbox.y     = entity.hitboxDefaultY;
                targets[i].hitbox.x = targets[i].hitboxDefaultX;
                targets[i].hitbox.y = targets[i].hitboxDefaultY;
            }
        }

        return index;
    }
}