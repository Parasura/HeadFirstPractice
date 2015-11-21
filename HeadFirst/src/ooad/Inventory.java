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
				continue;
			}
			
			String model = searchGuitar.getModel();
			if((model != null) && (!model.equals("")) && (!model.equals(gt.getModel()))){
				continue;
			}
			
			String type = searchGuitar.getType();
			if((type != null) && (!type.equals("")) && (!type.equals(gt.getType()))){
				continue;
			}
			
			String backWood = searchGuitar.getBackWood();
			if((backWood != null) && (!backWood.equals("")) && (!backWood.equals(gt.getBackWood()))){
				continue;
			}
			
			String topWood = searchGuitar.getTopWood();
			if((topWood != null) && (!topWood.equals("")) && (!topWood.equals(gt.getTopWood()))){
				continue;
			}
		}
		return null;
	}

}
