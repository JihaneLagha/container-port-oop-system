package Containers;

public class HeavyContainer extends Container{
	
		public HeavyContainer(int ID, int weight) {
			super(ID, weight);
		}
		
		@Override
		public double consumption() {
			return 3.00 * weight;
		}
		
		@Override
		public boolean equals(Container other) {
			return other.getClass().getSimpleName().equals("HeavyContainer") && ID == other.ID && weight == other.weight;
		}

}
