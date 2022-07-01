import java.awt.*;
import java.awt.geom.AffineTransform;

public class WorldZone extends Rectangle {
    private final WorldArea worldArea;

    public WorldZone(int x, int y, int width, int height, WorldArea worldArea) {
        super(x, y, width, height);
        this.worldArea = worldArea;
    }

    public WorldArea getWorldArea() {
        return worldArea;
    }

    public void draw(Graphics g) {
        g.setColor(worldArea.getColor());
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
        int ty = y + height/2 + fm.stringWidth(worldArea.getName())/4 + fm.getAscent()/8;

        g2.drawString(worldArea.getName(), tx, ty);
    }
}
