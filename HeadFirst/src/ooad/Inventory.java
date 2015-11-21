package ooad;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

	private List<Guitar> guitars;

	public Inventory() {
		guitars = new ArrayList<>();
	}

	public void addGuitar(String serialNumber, double price, String builder, String model, String type, String backWood, String topWood) {
		Guitar guitar = new Guitar(serialNumber, price, builder, model, type, backWood, topWood);
		guitars.add(guitar);
	}

	public Guitar getGuitar(String serialNumber) {
		for (Guitar gt : guitars) {
			if (gt.getSerialNumber() == serialNumber) {
				return gt;
			}
		}
		return null;
	}
	
	public Guitar search(Guitar searchGuitar){
		for(Guitar gt : guitars){
			String builder = searchGuitar.getBuilder();
			if((builder != null) && (!builder.equals("")) && (!builder.equals(gt.getBuilder()))){
				
			}
		}
		return null;
	}

}
