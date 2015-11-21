package practice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class SortSongs {

	
	
	static List<Songs> lt = new ArrayList<Songs>();

	public static void main(String[] args) throws IOException {
		new SortSongs().doMethod();

	}

	private void doMethod() throws FileNotFoundException, IOException {
		File file = new File("D:\\Songs.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("/");
			Songs obj = new Songs(tokens[0], tokens[1]);
			lt.add(obj);
		}
		System.out.println(lt);
		Collections.sort(lt);
		System.out.println(lt);
		
		class Inner implements Comparator<Songs>{

			public int compare(Songs o1, Songs o2) {
				return o1.year.compareTo(o2.year);
			}
		}
			
		Inner obj = new Inner();
		Collections.sort(lt,obj);
		System.out.println(lt);
			
		}
	}


