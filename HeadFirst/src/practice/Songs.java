package practice;

public class Songs implements Comparable<Songs> {

	String title;
	String year;

	Songs(String title, String year) {
		this.title = title;
		this.year = year;
	}

	public String toString() {
		return title + " " + year;

	}

	@Override
	public int compareTo(Songs o) {
		
		return this.title.compareTo(o.title);
	}

}
