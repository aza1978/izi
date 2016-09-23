package model;

public class Animal implements Comparable<Animal> {

	public enum Species {
		BIRD, MARSUPIAL, MYTHICAL, CANINE
	}

	public Animal(Species species, String type, Integer eatsPerMeal) {
		this.species = species;
		this.type = type;
		this.eatsPerMeal = eatsPerMeal;
		timesFed = 0;
		overFed = 0;
		underFed = 0;
		feedTotal = 0;
	}

	private String type;

	private Integer eatsPerMeal;

	private Integer timesFed;

	private Integer underFed;

	private Integer overFed;

	private Integer feedTotal;

	private Species species;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getEatsPerMeal() {
		return eatsPerMeal;
	}

	public void setEatsPerMeal(Integer eatsPerMeal) {
		this.eatsPerMeal = eatsPerMeal;
	}

	public Integer getTimesFed() {
		return timesFed;
	}

	public void setTimesFed(Integer timesFed) {
		this.timesFed = timesFed;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public Integer getUnderFed() {
		return underFed;
	}

	public void setUnderFed(Integer underFed) {
		this.underFed = underFed;
	}

	public Integer getOverFed() {
		return overFed;
	}

	public void setOverFed(Integer overFed) {
		this.overFed = overFed;
	}

	public Integer getFeedTotal() {
		return feedTotal;
	}

	public void setFeedTotal(Integer feedTotal) {
		this.feedTotal = feedTotal;
	}

	@Override
	public int compareTo(Animal animal) {
		return this.type.compareTo(animal.getType());
	}

}
