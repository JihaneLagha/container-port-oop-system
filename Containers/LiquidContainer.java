package Containers;

public class LiquidContainer extends HeavyContainer{
	
		public LiquidContainer(int ID, int weight) {
			super(ID, weight);
		}
		
		@Override
		public double consumption() {
			return 4.00 * weight;
		}
		
		@Override
		public boolean equals(Container other) {
			return other.getClass().getSimpleName().equals("LiquidContainer") && ID == other.ID && weight == other.weight;
		}

}
