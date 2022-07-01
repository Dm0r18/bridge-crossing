import java.awt.*;
import java.util.ArrayList;

public class WorldMap {
    private static WorldMap instance = null;
    private static Dimension worldSize;
    private ArrayList<WorldZone> worldZones;
    private int mapZoneWidth;
    private int mapZoneHeight;

    private WorldMap() {
        calculateMapZoneSize();
        createWorldMap();
    }

    public static WorldMap getInstance() {
        if(instance == null) {
            instance = new WorldMap();
        }
        return instance;
    }

    public static WorldMap setDimension(Dimension d) {
        worldSize = d;
        return getInstance();
    }

    private void calculateMapZoneSize() {
        int numberOfTiles = 0;
        for(WorldArea type : WorldArea.values()) {
            numberOfTiles += type.getWidthRatio();
        }
        mapZoneWidth = (int) (worldSize.getWidth()/numberOfTiles);
        mapZoneHeight = (int) worldSize.getHeight();
    }

    private void createWorldMap(){
        worldZones = new ArrayList<>();
        int y = 0;
        int height = mapZoneHeight;
        int x = 0;
        int width = 0;

        for(WorldArea type : WorldArea.values()) {
            x += width;
            width = mapZoneWidth * type.getWidthRatio() ;
            worldZones.add(new WorldZone(x, y, width, height, type));
        }
    }

    public int getWidth() {
        return (int) worldSize.getWidth();
    }

    public int getHeight() {
        return (int) worldSize.getHeight();
    }

    public WorldZone getWorldZone(WorldArea type) {
        for(WorldZone worldZone : worldZones) {
            if(worldZone.getWorldArea() == type) {
                return worldZone;
            }
        }
        return null;
    }

    public Dimension getWorldZoneSize(WorldArea type) {
        WorldZone worldZone = getWorldZone(type);
        if(worldZone == null)
            return null;
        else
            return worldZone.getSize();
    }

    public int getWorldZoneWidth(WorldArea type) {
        Dimension size = getWorldZoneSize(type);
        if(size == null)
            return 0;
        else
            return (int) size.getWidth();
    }

//    public int getWorldZoneHeight(WorldArea type) {
//        Dimension size = getWorldZoneSize(type);
//        if(size == null)
//            return 0;
//        else
//            return (int) size.getHeight();
//    }
//
//    public int getWorldZoneX(WorldArea type) {
//        WorldZone worldZone = getWorldZone(type);
//        if(worldZone == null)
//            return -1;
//        else
//            return (int) worldZone.getX();
//    }
//
//    public int getWorldZoneY(WorldArea type) {
//        WorldZone worldZone = getWorldZone(type);
//        if(worldZone == null)
//            return -1;
//        else
//            return (int) worldZone.getY();
//    }

    public void draw(Graphics g) {
        for(WorldZone worldZone : worldZones) {
            worldZone.draw(g);
        }
    }

    public WorldArea getWorldArea(int x) {
        for(WorldZone worldZone : worldZones) {
            if(worldZone.contains(x,0) ) {
                return worldZone.getWorldArea();
            }
        }
        return null;
    }
}
