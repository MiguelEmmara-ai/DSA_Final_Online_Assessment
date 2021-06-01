package Miguel.Industries;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Miguel Emmara - 18022146
 */
public class FactorySimulatorGUI extends JPanel implements ActionListener, ChangeListener {
    public static int PANEL_H = 500;
    public static int PANEL_W = 700;

    private final JButton addDispatcherButton;
    private final JButton removeDispatcherButton;
    private final JSlider maxConsumptionSlider;
    private final JPanel maxConsumptionSliderPanel;

    private final JButton addMachineButton;
    private final JButton removeMachineButton;
    private final JSlider maxProductionSlider;
    private final JPanel maxProductionSliderPanel;

    private final JPanel southPanel, northPanel, eastPanel, westPanel;

    private static ConveyorBelt[] conveyorBelts;
    private DrawPanel drawPanel;
    private Machine machine;
    private Dispatcher dispatcher;
    private ArrayList<Machine> machines;
    private ArrayList<Dispatcher> dispatchers;
    private final JLabel topStatusLabel;

    private final Timer timer = new Timer(5, this);

    public FactorySimulatorGUI() {
        super(new BorderLayout());

        /*try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}*/

        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));

        topStatusLabel = new JLabel();

        addDispatcherButton = new JButton("Add Dispatcher");
        addDispatcherButton.addActionListener(this);
        addDispatcherButton.setFocusable(false);
        removeDispatcherButton = new JButton("Remove Dispatcher");
        removeDispatcherButton.addActionListener(this);
        removeDispatcherButton.setFocusable(false);
        maxConsumptionSlider = new JSlider(10, 2000);
        maxConsumptionSlider.addChangeListener(this);
        maxConsumptionSlider.setFocusable(false);
        maxConsumptionSliderPanel = new JPanel();
        maxConsumptionSliderPanel.setBorder(BorderFactory.createTitledBorder("Max Consumption Time"));

        addMachineButton = new JButton("Add Machine");
        addMachineButton.addActionListener(this);
        addMachineButton.setFocusable(false);
        removeMachineButton = new JButton("Remove Machine");
        removeMachineButton.addActionListener(this);
        removeMachineButton.setFocusable(false);
        maxProductionSlider = new JSlider(10, 2000);
        maxProductionSlider.addChangeListener(this);
        maxProductionSlider.setFocusable(false);
        maxProductionSliderPanel = new JPanel();
        maxProductionSliderPanel.setBorder(BorderFactory.createTitledBorder("Max Production Time"));

        southPanel = new JPanel();
        northPanel = new JPanel();
        eastPanel = new JPanel();
        westPanel = new JPanel();

        getEastPanel().add(getAddDispatcherButton());
        getEastPanel().add(getRemoveDispatcherButton());

        getMaxConsumptionSliderPanel().add(getMaxConsumptionSlider());

        getWestPanel().add(getAddMachineButton());
        getWestPanel().add(getRemoveMachineButton());

        getMaxProductionSliderPanel().add(getMaxProductionSlider());

        getSouthPanel().add(getEastPanel());
        getSouthPanel().add(getMaxConsumptionSliderPanel());
        getSouthPanel().add(getWestPanel());
        getSouthPanel().add(getMaxProductionSliderPanel());

        getNorthPanel().add(getTopStatusLabel());

        setDrawPanel(new DrawPanel());

        add(getSouthPanel(), BorderLayout.SOUTH);
        add(getNorthPanel(), BorderLayout.NORTH);
        add(getDrawPanel(), BorderLayout.CENTER);

        setMachines(new ArrayList<>());
        setDispatchers(new ArrayList<>());

        getTimer().start();

        setConveyorBelts(new ConveyorBelt[5]);

        for (int i = 0; i < 5; i++) {
            getConveyorBelts()[i] = new ConveyorBelt(8);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == getAddMachineButton()) {
            setMachine(new Machine(getConveyorBelts()));
            getMachines().add(getMachine());
        }

        if (source == getAddDispatcherButton()) {
            setDispatcher(new Dispatcher(getConveyorBelts()));
            getDispatchers().add(getDispatcher());
        }

        if (source == getRemoveMachineButton()) {
            if (getMachines().size() <= 0)
                JOptionPane.showMessageDialog(null, "There Are No Machines To Remove!", "Error!", JOptionPane.ERROR_MESSAGE);
            else {
                int i = getMachines().size() - 1;

                setMachine(getMachines().get(i));
                getMachine().getThready().stop();

                for (int j = 0; j < getConveyorBelts().length; j++) {
                    getConveyorBelts()[j].disconnectMachine(getMachine());
                }

                getMachines().remove(i);
            }
        }

        if (source == getRemoveDispatcherButton()) {
            if (getDispatchers().size() <= 0)
                JOptionPane.showMessageDialog(null, "There Are No Dispatchers To Remove!", "Error!", JOptionPane.ERROR_MESSAGE);
            else {
                int i = getDispatchers().size() - 1;

                setDispatcher(getDispatchers().get(i));
                getDispatcher().getThready().stop();

                for (int j = 0; j < getConveyorBelts().length; j++) {
                    getConveyorBelts()[j].disconnectDispatcher(getDispatcher());
                }

                getDispatchers().remove(i);
            }
        }

        if (source == getTimer()) {
            getDrawPanel().repaint();
        }
    }

    // if JSlider value is changed
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();

        if (source == getMaxConsumptionSlider()) {
            Machine.MAX_CONSUMPTION_TIME = source.getValue();
        }

        if (source == getMaxProductionSlider()) {
            Machine.MAX_PRODUCTION_TIME = source.getValue();
        }
    }

    // Inner Class DrawPanel
    private class DrawPanel extends JPanel {
        public DrawPanel() {
            super();
            setPreferredSize(new Dimension(500, 500));
            //setBackground(Color.white);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            getTopStatusLabel().setText("Number of Dispatchers = " + getDispatchers().size()
                    + ", Number of Machines = " + getMachines().size());

            for (int i = 0; i < getConveyorBelts().length; i++) {
                getConveyorBelts()[i].drawBelt(g, 150, 100 + i * 100, 1000, 100);

            }
        }
    }

    // GETTER & SETTERS

    public JButton getAddDispatcherButton() {
        return addDispatcherButton;
    }

    public JButton getRemoveDispatcherButton() {
        return removeDispatcherButton;
    }

    public JSlider getMaxConsumptionSlider() {
        return maxConsumptionSlider;
    }

    public JPanel getMaxConsumptionSliderPanel() {
        return maxConsumptionSliderPanel;
    }

    public JButton getAddMachineButton() {
        return addMachineButton;
    }

    public JButton getRemoveMachineButton() {
        return removeMachineButton;
    }

    public JSlider getMaxProductionSlider() {
        return maxProductionSlider;
    }

    public JPanel getMaxProductionSliderPanel() {
        return maxProductionSliderPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public JPanel getEastPanel() {
        return eastPanel;
    }

    public JPanel getWestPanel() {
        return westPanel;
    }

    public static ConveyorBelt[] getConveyorBelts() {
        return conveyorBelts;
    }

    public static void setConveyorBelts(ConveyorBelt[] conveyorBelts) {
        FactorySimulatorGUI.conveyorBelts = conveyorBelts;
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }

    public void setDrawPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ArrayList<Machine> getMachines() {
        return machines;
    }

    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;
    }

    public ArrayList<Dispatcher> getDispatchers() {
        return dispatchers;
    }

    public void setDispatchers(ArrayList<Dispatcher> dispatchers) {
        this.dispatchers = dispatchers;
    }

    public JLabel getTopStatusLabel() {
        return topStatusLabel;
    }

    public Timer getTimer() {
        return timer;
    }
}
