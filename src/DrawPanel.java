import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class DrawPanel extends JPanel implements Runnable {
    private Controller controller;

    public DrawPanel() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    public void setSimulationManager(Controller controller) {
        this.controller = controller;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(controller != null) {
            controller.draw(g);
        }
    }

    @Override
    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                System.err.println("Sleep error");
            }
        }
    }
}