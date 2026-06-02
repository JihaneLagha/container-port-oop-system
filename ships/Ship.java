package ships;
import java.util.ArrayList;
import java.util.Collections;
import Containers.Container;
import Containers.HeavyContainer;
import Containers.LiquidContainer;
import Containers.RefrigeratedContainer;
import interfaces.IShip;
import ports.Port;


public class Ship implements IShip, Comparable<Ship> {
	
		private int ID;
		private double fuel;
		private Port currentPort;
		private ArrayList<Container> containers;
		private int totalWeightCapacity;
		private int maxNumberOfAllContainers;
		private int maxNumberOfHeavyContainers;
		private int maxNumberOfRefrigeratedContainers;
		private int maxNumberOfLiquidContainers;
		private double fuelConsumptionPerKM;
		private int totalWeight;
		private int numberOfAllContainers;
		private int numberOfHeavyContainers;
		private int numberOfRefrigeratedContainers;
		private int numberOfLiquidContainers;
		
		public Ship(int ID, Port p, int totalWeightCapacity, int maxNumberOfAllContainers, int maxNumberOfHeavyContainers,
				int maxNumberOfRefrigeratedContainers, int maxNumberOfLiquidContainers, double fuelConsumptionPerKM) {
			this.ID = ID;
			fuel = 0.0;
			currentPort = p;
			currentPort.incomingShip(this);
			
			containers = new ArrayList<Container>();
			this.totalWeightCapacity = totalWeightCapacity;
			this.maxNumberOfAllContainers = maxNumberOfAllContainers;
			this.maxNumberOfHeavyContainers = maxNumberOfHeavyContainers;
			this.maxNumberOfRefrigeratedContainers = maxNumberOfRefrigeratedContainers;
			this.maxNumberOfLiquidContainers = maxNumberOfLiquidContainers;
			this.fuelConsumptionPerKM = fuelConsumptionPerKM;
			totalWeight = 0;
			numberOfAllContainers = 0;
			numberOfHeavyContainers = 0;
			numberOfRefrigeratedContainers = 0;
			numberOfLiquidContainers = 0;
		}
		
		public ArrayList<Container> getCurrentContainers() {
			Collections.sort(containers);
			return containers;
		}
		
		@Override
		public boolean sailTo(Port p) {
			double total_fuel_cost = fuelConsumptionPerKM;
			for(Container cont : containers)
				total_fuel_cost += cont.consumption();
			total_fuel_cost *= currentPort.getDistance(p);
			
			boolean can_sail = total_fuel_cost <= fuel;
			
			if(can_sail) {
				fuel -= total_fuel_cost;
				currentPort.getCurrent().remove(this);
				currentPort.outgoingShip(this);
				p.incomingShip(this);
				currentPort = p;
			}
			
			return can_sail;
		}
		
		@Override
		public void reFuel(double newFuel) {
			if(newFuel < 0.0)
				throw new IllegalArgumentException();
			
			fuel += newFuel;
		}
		
		@Override
		public boolean load(Container cont) {
			boolean can_load;
			
			if(!currentPort.getContainers().contains(cont) ||
				numberOfAllContainers >= maxNumberOfAllContainers ||
				totalWeight + cont.getWeight() > totalWeightCapacity)
				can_load = false;
			else if(cont instanceof HeavyContainer) {
				if(numberOfHeavyContainers >= maxNumberOfHeavyContainers)
					can_load = false;
				else if(cont instanceof RefrigeratedContainer)
					can_load = numberOfRefrigeratedContainers < maxNumberOfRefrigeratedContainers;
				else if(cont instanceof LiquidContainer)
					can_load = numberOfLiquidContainers < maxNumberOfLiquidContainers;
				else
					can_load = true;
			}
			else
				can_load = true;
			
			if(can_load) {
				currentPort.getContainers().remove(cont);
				containers.add(cont);
				numberOfAllContainers++;
				totalWeight += cont.getWeight();
				if(cont instanceof HeavyContainer) {
					numberOfHeavyContainers++;
					if(cont instanceof RefrigeratedContainer)
						numberOfRefrigeratedContainers++;
					else if(cont instanceof LiquidContainer)
						numberOfLiquidContainers++;
				}
			}
			
			return can_load;
		}
		
		@Override
		public boolean unLoad(Container cont) {
			boolean can_unLoad = containers.contains(cont);
			
			if(can_unLoad) {
				containers.remove(cont);
				currentPort.getContainers().add(cont);
				numberOfAllContainers--;
				totalWeight -= cont.getWeight();
				if(cont instanceof HeavyContainer) {
					numberOfHeavyContainers--;
					if(cont instanceof RefrigeratedContainer)
						numberOfRefrigeratedContainers--;
					else if(cont instanceof LiquidContainer)
						numberOfLiquidContainers--;
				}
			}
			
			return can_unLoad;
		}
		
		@Override
	    public int compareTo(Ship other) {
	        return ID < other.ID ? -1 : (ID == other.ID ? 0 : 1);
	    }
		
		@Override
		public String toString() {
			String str = String.format("  Ship %d: %.2f\n", ID, fuel);
			ArrayList<ArrayList<Container>> sorted = Container.getContainersSortedByType(containers);
			for(ArrayList<Container> conts : sorted)
				if(!conts.isEmpty()) {
					str += "    " + conts.get(0).getClass().getSimpleName() + ":";
					for(Container cont : conts)
						str += " " + cont.getID();
					str += "\n";
				}
					
			return str;
		}

}
