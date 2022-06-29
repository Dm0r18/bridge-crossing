import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class WorldMap {

    private class WorldZone extends Rectangle {

        private static final long serialVersionUID = -6198608761521202696L;

        private WorldArea worldZone;

        public WorldZone(int x, int y, int width, int height, WorldArea worldZone) {
            super(x, y, width, height);
            this.worldZone = worldZone;
        }

        public WorldArea getWorldZoneType() {
            return worldZone;
        }

        public void draw(Graphics g) {
            g.setColor(worldZone.getColor());
            g.fillRect(x, y, width, height);

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(-Math.PI/2);

            Font font = new Font("Arial", Font.BOLD, 24);
            Font rotatedFont = font.deriveFont(affineTransform);

            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(rotatedFont);
            g2.setColor(Color.WHITE);

            FontMetrics fm = g2.getFontMetrics(font);
            int tx = x + width/2 + fm.getHeight()/4;
            int ty = y + height/2 + fm.stringWidth(worldZone.getName())/4 + fm.getAscent()/8;

            g2.drawString(worldZone.getName(), tx, ty);
        }
    }

    private ArrayList<WorldZone> worldZones;

    private Dimension worldSize;

    private int mapZoneWidth;
    private int mapZoneHeight;

    public WorldMap(Dimension worldSize) {
        this.worldSize = worldSize;
        calculateMapZoneSize();
        createWorldMap();
    }


    private void calculateMapZoneSize() {
        int numberOfTiles = 0;
        for(WorldArea type : WorldArea.values()) {
            numberOfTiles += type.getWidthRatio();
        }
        mapZoneWidth = (int) (this.worldSize.getWidth()/numberOfTiles);
        mapZoneHeight = (int) this.worldSize.getHeight();
    }

    private void createWorldMap(){
        worldZones = new ArrayList<WorldZone>();

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

    public WorldZone getWorldZone(WorldArea type) {
        for(WorldZone worldZone : worldZones) {
            if(worldZone.getWorldZoneType() == type) {
                return worldZone;
            }
        }
        return null;
    }

    public Dimension getSize() {
        return worldSize;
    }

    public int getWidth() {
        return (int) worldSize.getWidth();
    }

    public int getHeight() {
        return (int) worldSize.getHeight();
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

    public int getWorldZoneHeight(WorldArea type) {
        Dimension size = getWorldZoneSize(type);
        if(size == null)
            return 0;
        else
            return (int) size.getHeight();
    }

    public int getWorldZoneX(WorldArea type) {
        WorldZone worldZone = getWorldZone(type);
        if(worldZone == null)
            return -1;
        else
            return (int) worldZone.getX();
    }

    public int getWorldZoneY(WorldArea type) {
        WorldZone worldZone = getWorldZone(type);
        if(worldZone == null)
            return -1;
        else
            return (int) worldZone.getY();
    }

    public void draw(Graphics g) {
        for(WorldZone worldZone : worldZones) {
            worldZone.draw(g);
        }
    }

}