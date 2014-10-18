/*Yaroslav Trach, Aakash Sethi, Matt Mans, Verek Rananujan
 * DubHacks 2014
 *
 *Takes network traffic data from RequestTracker, and 
 *generate bar graphs of data.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class BarGraph {
    public static void main(String[] args) throws FileNotFoundException {
    	File dumpFile = new File("dumpfile.txt");
    	//create a map for the dumpfile.txt
    	RequestTracker tracker = new RequestTracker(dumpFile);
    	
        //create a bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Map<String, Integer> map = tracker.getFrequencies();
        
        //make a for-each loop that will go throw all the data
        for(String company: map.keySet()){
        	dataset.setValue(map.get(company), "Hits", company);
        }
        
        //make the bar graph
        JFreeChart barChart = ChartFactory.createBarChart("DubHacks 2014 API Popularity",
        "Company", "Hits", dataset, PlotOrientation.VERTICAL,
        false, true, false);
      
        try {
        	//save the bar graph as jpeg
            ChartUtilities.saveChartAsJPEG(new File("C:\\barChart.jpg"), barChart, 1200, 1000);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        } 
    }
}
