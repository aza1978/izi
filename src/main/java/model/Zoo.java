package model;

public class Zoo implements Comparable<Zoo> {

	private String name;

	private Integer inventory;

	private Integer waste;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public Integer getWaste() {
		return waste;
	}

	public void setWaste(Integer waste) {
		this.waste = waste;
	}

	@Override
	public int compareTo(Zoo zoo) {
		return this.name.compareTo(zoo.getName());
	}

}
