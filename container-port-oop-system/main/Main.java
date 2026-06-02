package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.*;
import java.io.*;
import java.awt.*;

import javax.swing.*;

import Containers.Container;
import Containers.HeavyContainer;
import Containers.RefrigeratedContainer;
import Containers.BasicContainer;
import Containers.LiquidContainer;
import ports.Port;
import ships.Ship;

public class Main extends JFrame{

	private JTextField inputFileField, outputFileField;
    private JButton processButton;

    public Main() {
        super("File Processor");
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create input file field
        JLabel inputLabel = new JLabel("Input File:");
        inputFileField = new JTextField(20);
        JButton browseInputButton = new JButton("Browse");
        browseInputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    inputFileField.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });

        // Create output file field
        JLabel outputLabel = new JLabel("Output File:");
        outputFileField = new JTextField(20);
        JButton browseOutputButton = new JButton("Browse");
        browseOutputButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    outputFileField.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });

        // create a file object for the current location
        File file = new File("output.txt");

        try {

            // create a new file with name specified
            // by the file object
            boolean value = file.createNewFile();
            if (value) {
                System.out.println("New output File is created.");
            } else {
                System.out.println("The file already exists.");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }

        // Create process button
        processButton = new JButton("Process");
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputFilePath = inputFileField.getText();
                String outputFilePath = outputFileField.getText();
                processFile(inputFilePath, outputFilePath);
            }
        });

        // Add components to the frame
        add(inputLabel);
        add(inputFileField);
        add(browseInputButton);
        add(outputLabel);
        add(outputFileField);
        add(browseOutputButton);
        add(processButton);

        pack();
        setVisible(true);
    }

    private void processFile(String inputFilePath, String outputFilePath) {
		try {
			Scanner in = new Scanner(new File(inputFilePath));
	
			final int N = in.nextInt();
	
			ArrayList<Container> conts = new ArrayList<Container>();
			ArrayList<Ship> ships = new ArrayList<Ship>();
			ArrayList<Port> ports = new ArrayList<Port>();
	
			for (int i = 0; i < N; i++) {
				final int operation_type = in.nextInt();
				switch (operation_type) {
					case 1: {
						final int cont_ID = conts.size();
						final int port_ID = in.nextInt();
						final int weight = in.nextInt();
						Container cont;
						if (in.hasNextInt()) {
							if (weight > 3000)
								cont = new HeavyContainer(cont_ID, weight);
							else
								cont = new BasicContainer(cont_ID, weight);
						} else {
							final char special_type = in.next().charAt(0);
							if (special_type == 'L')
								cont = new LiquidContainer(cont_ID, weight);
							else
								cont = new RefrigeratedContainer(cont_ID, weight);
						}
	
						ports.get(port_ID).getContainers().add(cont);
						conts.add(cont);
						break;
					}
	
					case 2: {
						final int ship_ID = ships.size();
						final int port_ID = in.nextInt();
						final int totalWeightCapacity = in.nextInt();
						final int maxNumberOfAllContainers = in.nextInt();
						final int maxNumberOfHeavyContainers = in.nextInt();
						final int maxNumberOfRefrigeratedContainers = in.nextInt();
						final int maxNumberOfLiquidContainers = in.nextInt();
						final double fuelConsumptionPerKM = in.nextDouble();
	
						ships.add(new Ship(ship_ID, ports.get(port_ID), totalWeightCapacity,
								maxNumberOfAllContainers, maxNumberOfHeavyContainers, maxNumberOfRefrigeratedContainers,
								maxNumberOfLiquidContainers, fuelConsumptionPerKM));
						break;
					}
	
					case 3: {
						final int port_ID = ports.size();
						final double X = in.nextDouble();
						final double Y = in.nextDouble();
	
						ports.add(new Port(port_ID, X, Y));
						break;
					}
	
					case 4: {
						final int ship_ID = in.nextInt();
						final int cont_ID = in.nextInt();
						ships.get(ship_ID).load(conts.get(cont_ID));
						break;
					}
	
					case 5: {
						final int ship_ID = in.nextInt();
						final int cont_ID = in.nextInt();
						ships.get(ship_ID).unLoad(conts.get(cont_ID));
						break;
					}
	
					case 6: {
						final int ship_ID = in.nextInt();
						final int port_ID = in.nextInt();
						ships.get(ship_ID).sailTo(ports.get(port_ID));
						break;
					}
	
					case 7: {
						final int ship_ID = in.nextInt();
						final double fuel = in.nextDouble();
						ships.get(ship_ID).reFuel(fuel);
						break;
					}
	
					default:
						System.out.println("Invalid operation.");
				}
			}
	
			in.close();
	
			PrintStream out = new PrintStream(new File(outputFilePath));
	
			for (Port port : ports)
				out.print(port.toString());
	
			out.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    }
	public static void main(String[] args){

		/*Main obj=new Main();
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.setSize(500,500);
		obj.setVisible(true);
*/	
			SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });

	}
}
