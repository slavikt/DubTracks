/*Yaroslav Trach, Aakash Sethi, Matt Mans, Verek Rananujan
 * DubHacks 2014
 * 
 * Processes and parses the data from the text file where all of the 
 * information is stored.
 */

import java.util.*;
import java.io.*;
import java.lang.*;

public class RequestTracker {

	private Map<String, Integer> frequencyMap;
	private Map<String, RequestNode> segmentMap;
	private RequestNode front;

	public RequestTracker(File dumpFile) throws FileNotFoundException {
		frequencyMap = new HashMap<String, Integer>();
		segmentMap = new HashMap<String, RequestNode>();
		Scanner fileReader = new Scanner(dumpFile);

		// Initialize frequency map
		frequencyMap.put("Mashery", 0);
		frequencyMap.put("Mojio", 0);
		frequencyMap.put("Twilio", 0);
		frequencyMap.put("Zillow", 0);
		frequencyMap.put("Amazon", 0);
		frequencyMap.put("Codeship", 0);
		frequencyMap.put("Microsoft", 0);
		frequencyMap.put("Sendgrid", 0);
		frequencyMap.put("Google", 0);
		frequencyMap.put("Whitepages", 0);

		if (fileReader.hasNextLine()) {
			String line = fileReader.nextLine();
			String keyword = getKeyword(line);
			String timestamp = line.substring(0, 8);
			front = new RequestNode(keyword, timestamp);
			segmentMap.put(timestamp, front);
			frequencyMap.put(keyword, 1);
			RequestNode previous = front;
			RequestNode current = front.next;
			char oldMinute = timestamp.charAt(4);
			while (fileReader.hasNextLine()) {
				line = fileReader.nextLine();
				keyword = getKeyword(line);
				if (keyword == "") {
					continue;
				}
				timestamp = line.substring(0, 8);
				char newMinute = timestamp.charAt(4);
				if (frequencyMap.containsKey(keyword)) {
					frequencyMap.put(keyword, frequencyMap.get(keyword) + 1);
				} else {
					frequencyMap.put(keyword, 1);
				}
				current = new RequestNode(keyword, timestamp);
				if (newMinute != oldMinute) {
					segmentMap.put(timestamp, current);
				}
				previous.next = current;
				previous = current;
				current = current.next;
				oldMinute = newMinute;
			}
			front = front.next;
			frequencyMap.remove("");
			System.out.println(frequencyMap);
		}
		fileReader.close();
	}

	public Map<String, Integer> getFrequencies() {
		return frequencyMap;
	}

	public Map<String, Integer> getFrequencies(String startTime, String endTime) {
		Map<String, Integer> timeFrameFrequencies = new HashMap<String, Integer>();

		double deltaMinTest = 10000000;
		double deltaMaxTest = 10000000;
		double deltaMin = 100000;
		double deltaMax = 100000;
		String startKey = null;
		String endKey = null;
		RequestNode startNode = null;
		RequestNode endNode = null;

		for (String key : segmentMap.keySet()) {
			deltaMinTest = subtractTime(key, startTime);
			deltaMaxTest = subtractTime(key, endTime);
			if (deltaMinTest < deltaMin) {
				deltaMin = deltaMinTest;
				startKey = key;
			}
			if (deltaMaxTest < deltaMax) {
				deltaMax = deltaMaxTest;
				endKey = key;
			}
			startNode = segmentMap.get(startKey);
			endNode = segmentMap.get(endKey);
		}

		RequestNode current = startNode;
		System.out.println("Starting: " + startNode.timestamp);
		System.out.println("Ending: " + endNode.timestamp);
  		while (current.next != null && current != endNode) {
  			System.out.print(current.timestamp + " ");
  			if (!timeFrameFrequencies.containsKey(current.website)) {
  				timeFrameFrequencies.put(current.website, 1);
  			} else {
  				timeFrameFrequencies.put(current.website, timeFrameFrequencies.get(current.website) + 1);
  			}
  			current = current.next;
  		}
		return timeFrameFrequencies;
	}

	private String getKeyword(String line) {
		String keyword = "";
		if (line.contains("mashery")) {
			keyword = "Mashery";
		} else if (line.contains("mojio")) {
			keyword = "Mojio";
		} else if (line.contains("twilio")) {
			keyword = "Twilio";
		} else if (line.contains("zillow")) {
			keyword = "Zillow";
		} else if (line.contains("codeship")) {
			keyword = "Codeship";
		} else if (line.contains("microsoft")) {
			keyword = "Microsoft";
		} else if (line.contains("sendgrid")) {
			keyword = "Sendgrid";
		} else if (line.contains("amazon")) {
			keyword = "Amazon";
		} else if (line.contains("newrelic")) {
			keyword = "Newrelic";
		} else if (line.contains("google")) {
			keyword = "Google";
		} else if (line.contains("whitepages")) {
			keyword = "Whitepages";
		}

		return keyword;
	}

	private int subtractTime(String timeOne, String timeTwo) {
		int timeOneNum = Integer.parseInt(timeOne.substring(0, 2)) * 3600
				+ Integer.parseInt(timeOne.substring(3, 5)) * 60;
		int timeTwoNum = Integer.parseInt(timeTwo.substring(0, 2)) * 3600
				+ Integer.parseInt(timeTwo.substring(3, 5)) * 60;

		return Math.abs(timeTwoNum - timeOneNum);
	}
}
