/**
 * 
 */
package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author yzc
 *
 */
public class MapUtil {

	/**
	 * Convert Map to List sorted by key, default as increasing order
	 * @param map the map to be converted
	 * @param sorting order, increasing or decreasing
	 * @return the sorted List
	 */
	public static<K extends Comparable<? super K>, V> List<Map.Entry<K, V>> mapToListSortedByKey(Map<K, V> map) {
		return mapToListSortedByKey(map, true);
	}

	/**
	 * Convert Map to List sorted by key
	 * @param map the map to be converted
	 * @param sorting order, increasing or decreasing
	 * @return the sorted List
	 */
	public static<K extends Comparable<? super K>, V> List<Map.Entry<K, V>> mapToListSortedByKey(Map<K, V> map, boolean increasing) {
		List<Map.Entry<K, V>> mapList = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(mapList, new Comparator<Map.Entry<K, V>>(){
			public int compare(Map.Entry<K, V> obj1, Map.Entry<K, V> obj2)
			{
				return increasing ? obj1.getKey().compareTo(obj2.getKey()) : obj2.getKey().compareTo(obj1.getKey());
			}
		});
		return mapList;
	}

	/**
	 * Convert Map to List sorted by value, default as decreasing order
	 * @param map the map to be converted
	 * @param sorting order, increasing or decreasing
	 * @return the sorted List
	 */
	public static<K, V extends Comparable<? super V>> List<Map.Entry<K, V>> mapToListSortedByValue(Map<K, V> map) {
		return mapToListSortedByValue(map, false);
	}

	/**
	 * Convert Map to List sorted by value
	 * @param map the map to be converted
	 * @param sorting order, increasing or decreasing
	 * @return the sorted List
	 */
	public static<K, V extends Comparable<? super V>> List<Map.Entry<K, V>> mapToListSortedByValue(Map<K, V> map, boolean increasing) {
		List<Map.Entry<K, V>> mapList = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(mapList, new Comparator<Map.Entry<K, V>>(){
			public int compare(Map.Entry<K, V> obj1, Map.Entry<K, V> obj2)
			{
				return increasing ? obj1.getValue().compareTo(obj2.getValue()) : obj2.getValue().compareTo(obj1.getValue());
			}
		});
		return mapList;
	}

	/**
	 * Display map in sorted order, default as by key and increasing order.
	 * @param map map to be output
	 */
	public static<K extends Comparable<? super K>, V extends Comparable<? super V>> void displayMapSortedOrder(Map<K, V> map) {
		displayMapSortedOrder(map, true, true);
	}

	/**
	 * Display map in sorted order.
	 * @param byKeyOrValue Boolean value, if true by key. Otherwise, by value.
	 * @param map Map to be displayed
	 * @param increasing Increasing or decreasing order
	 */
	public static<K extends Comparable<? super K>, V extends Comparable<? super V>> void displayMapSortedOrder(Map<K, V> map, boolean byKeyOrValue, boolean increasing) {
		List<Map.Entry<K, V>> mapList = byKeyOrValue ? mapToListSortedByKey(map, increasing) : mapToListSortedByValue(map, increasing);
		for (int i = 0; i < mapList.size(); i++) {
			System.out.print(mapList.get(i).getKey().toString());				
			System.out.print(":");				
			System.out.print(mapList.get(i).getValue().toString());				
			System.out.println();				
		}
	}

	/**
	 * Output map in sorted order, default as by key and increasing order.
	 * @param map map to be displayed
	 * @param fileName file name with path
	 */
	public static<K extends Comparable<? super K>, V extends Comparable<? super V>> void outputMapValueSortedOrder(Map<K, V> map, String fileName) {
		outputMapValueSortedOrder(map, fileName, true, true);
	}

	/**
	 * Output map in sorted order.
	 * @param map map to be output
	 * @param fileName file name with path
	 * @param increasing increasing or decreasing order
	 * @param byKeyOrValue 
	 */
	public static<K extends Comparable<? super K>, V extends Comparable<? super V>> void outputMapValueSortedOrder(Map<K, V> map, String fileName, boolean byKeyOrValue, boolean increasing) {
		List<Map.Entry<K, V>> mapList = byKeyOrValue ? mapToListSortedByKey(map, increasing) : mapToListSortedByValue(map, increasing);

		// Output to file
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = 0; i < mapList.size(); i++) {
				bufferedWriter.write(mapList.get(i).getKey().toString());				
				bufferedWriter.write(":");				
				bufferedWriter.write(mapList.get(i).getValue().toString());				
				bufferedWriter.write("\n");				
			}
			bufferedWriter.flush();
			bufferedWriter.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
