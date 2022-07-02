import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class NarrowBridgeApp extends JFrame implements ChangeListener{
	private static final String APP_TITLE = "Narrow Bridge Simulation";
	private static final int SPACE = 4;
	private final DrawPanel drawPanel = new DrawPanel();
	private final Controller controller = new Controller(drawPanel);

	private final JLabel maxCarOnBridgeLabel = new JLabel("Limit of cars crossing the bridge:", JLabel.RIGHT);
	private final JSpinner maxCarOnBridgeSpinner = new JSpinner(
			new SpinnerNumberModel(controller.getBridgeThroughput(), 1, 10, 1));


	public NarrowBridgeApp() {
		super(APP_TITLE);
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		maxCarOnBridgeSpinner.addChangeListener(this);
		createWindowLayout();
		setVisible(true);
		startSimulation();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(NarrowBridgeApp::new);
	}

	private void createWindowLayout() {		
		Border emptyBorder = BorderFactory.createEmptyBorder(SPACE, SPACE, SPACE, SPACE);
		Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(SPACE, SPACE));
		mainPanel.setBorder(emptyBorder);
		
		JPanel northPanel = new JPanel(new GridLayout(1, 2, SPACE, SPACE));
		
		JPanel northWestPanel = new JPanel();
		northWestPanel.setLayout(new GridLayout(4, 1, SPACE, SPACE));
		northWestPanel.setBorder(emptyBorder);
		northPanel.add(northWestPanel);

		JPanel northEastPanel = new JPanel();
		northEastPanel.setLayout(new GridLayout(3, 2, SPACE, SPACE));
		northEastPanel.setBorder(etchedBorder);
		northEastPanel.add(maxCarOnBridgeLabel);
		northEastPanel.add(maxCarOnBridgeSpinner);
		northPanel.add(northEastPanel);
		
		JPanel centerPanel = new JPanel(new GridLayout(1, 2, SPACE, SPACE));
		centerPanel.add(drawPanel);	

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		setContentPane(mainPanel);
	}

	private void startSimulation() {
		new Thread(controller, "CONTROLLER").start();
		new Thread(drawPanel, "DRAW_PANEL").start();
	}
	
	@Override
	public void stateChanged(ChangeEvent event) {
		Object eSource = event.getSource();
		if(eSource == maxCarOnBridgeSpinner) {
			updateBridgeThroughput();
		}	
	}

	private void updateBridgeThroughput() {
		int carLimit = (int) maxCarOnBridgeSpinner.getValue();
		controller.setBridgeThroughput(carLimit);
	}
}
