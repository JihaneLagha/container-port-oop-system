package Containers;

public class BasicContainer extends Container{
	
	public BasicContainer(int ID, int weight) {
		super(ID, weight);
	}
	
	@Override
	public double consumption() {
		return 2.50 * weight;
	}
	
	@Override
	public boolean equals(Container other) {
		return other.getClass().getSimpleName().equals("BasicContainer") && ID == other.ID && weight == other.weight;
	}

}
