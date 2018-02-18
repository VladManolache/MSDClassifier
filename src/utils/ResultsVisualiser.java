package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class ResultsVisualiser extends JFrame {

	private static final long serialVersionUID = 1L;

	public ResultsVisualiser(String applicationTitle, String chartTitle, String resultsFile) throws IOException {
		super(applicationTitle); 
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// This will create the dataset  
		XYSeriesCollection dataset = createDataset(resultsFile);
		 
		// Generate the graph
		JFreeChart chart = createChart(dataset, chartTitle);
         
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		
		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		
		// add it to our application
		setContentPane(chartPanel); 
	}
 

	/**
	 * Creates a sample dataset 
	 * @throws IOException 
	 */

	private  XYSeriesCollection createDataset(String fileName) throws IOException {
		 
		// Create the dataset
        XYSeriesCollection dataset = new XYSeriesCollection(); 
        XYSeries series = new XYSeries("XYGraph"); 
        
        // Add values for that dataset.
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try { 
            String line = br.readLine();
            int i = 0;
            
            while (line != null) {
            	series.add(i, (int)Double.parseDouble(line));
            	i++;
            	
                line = br.readLine();
            } 
        } finally {
            br.close();
        }
		
        // Add the series to your data set
        dataset.addSeries(series); 
		return dataset; 
	}


	/**
	 * Creates a chart
	 */

	private JFreeChart createChart(XYSeriesCollection dataset, String title) {

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Clasification results",
				"Test Segments",
				"Train Segments",
				dataset,
				PlotOrientation.VERTICAL,  // Plot Orientation
				true,                      // Show Legend
				true,                      // Use tooltips
				false                      // Configure chart to generate URLs?
				);
		return chart; 
	}
	
	@SuppressWarnings("unused")
	private void saveChart(JFreeChart chart, String chartPath) {
		 
      try {
          ChartUtilities.saveChartAsJPEG(new File(chartPath), chart, 500, 300);
      } catch (IOException e) {
          System.err.println("Problem occurred creating chart.");
      }
	}
} 
