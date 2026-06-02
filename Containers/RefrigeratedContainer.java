package Containers;

public class RefrigeratedContainer extends HeavyContainer{
	
		public RefrigeratedContainer(int ID, int weight) {
			super(ID, weight);
		}
		
		@Override
		public double consumption() {
			return 5.00 * weight;
		}
		
		@Override
		public boolean equals(Container other) {
			return other.getClass().getSimpleName().equals("RefrigeratedContainer") && ID == other.ID && weight == other.weight;
		}

}
