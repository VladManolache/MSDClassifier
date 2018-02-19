package utils;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import local.AccuracyCalculating;
import main.Configurations;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.title.TextTitle; 
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import data.genre.BootstrapGenres;
import data.genre.Genres;

public class DifferenceChart extends JFrame {

	private static final long serialVersionUID = 1L;
	ArrayList<String> correspondingRow;
	ArrayList<String> resultsMapping = ResultsManagement.getClassMappings();
	
	public static void run(String resultsFilePath) throws Exception {

		/*
		 *  Visualize the results as a PieChart.
		 *  Input: result_file_name
		 */
		DifferenceChart demo = new DifferenceChart("Results", "Here are the results...", resultsFilePath);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
	
	public DifferenceChart(String applicationTitle, String chartTitle, String resultsFile) throws Exception {
		super(applicationTitle); 
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// This will create the dataset  
		XYDataset dataset = createDataset(resultsFile);
		 
		// Generate the graph
		JFreeChart chart = createChart(dataset, chartTitle);
        chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
		ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true)); 
		String result = "Classification accuracy: "+AccuracyCalculating.calculateTotalAccuracy(resultsFile)+"%";
		TextTitle source = new TextTitle(result, new Font("Courier New", Font.PLAIN, 12));
		chart.addSubtitle(source);
		
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
	private  XYDataset createDataset(String fileName) throws IOException {
		 
		// Create the dataset
        XYSeriesCollection dataset = new XYSeriesCollection(); 
        ArrayList<XYSeries> classes = new ArrayList<XYSeries>();
        
        int runConfiguration = Configurations.runConfiguration;
		if (runConfiguration != 1) {  // Not single core execution
			if (Configurations.bootstrapMSD) {
				for(int i = 0; i < BootstrapGenres.values().length; i++) {
					classes.add(new XYSeries(i + ""));
				}
			}
			else {
				for(int i = 0; i < Genres.values().length; i++) {
					classes.add(new XYSeries(i + ""));
				}
			} 
		}
        
        // Add values for that dataset.
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			String split[];
			int i = 0;

			correspondingRow = new ArrayList<>();
			while ((line = br.readLine()) != null) {

				if (line.contains("labels")) {
					split = line.split(" ");
					for (int j = 1; j < split.length; j++) {
						if (Configurations.bootstrapMSD) {
							classes.add(new XYSeries(BootstrapGenres.valueFromId(Integer.parseInt(split[j]))));
						} else {
							classes.add(new XYSeries(Genres.valueFromId(Integer.parseInt(split[j]))));
						}
					}
				} else {
					split = line.split(" ", 2);
					int currentItem = ((int) Double.parseDouble(split[0]));

					// If hadoop is enabled, no need to map the data.
					if (runConfiguration != 1) {  // Not single core execution
						classes.get(currentItem).add(i, currentItem);
					} else {
						XYSeries series = classes.get(Integer.parseInt(resultsMapping.get(currentItem)));
						series.add(i, Integer.parseInt(resultsMapping.get(currentItem)));
					}

					i++;
				}
			}
		}

		for (XYSeries aClass : classes) {
			dataset.addSeries(aClass);
		}
		
		return dataset; 
	}

    /**
     * Creates a chart.
     *
     * @param dataset  the dataset.
	 *
	 * @return A chart.
	 */
	private JFreeChart createChart(XYDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createScatterPlot(
				"Clasification results",
				"Test data",
				"Class identification",
				dataset
//				PlotOrientation.VERTICAL,  // Plot Orientation
//				true,                      // Show Legend
//				true,                      // Use tooltips
//				false                      // Configure chart to generate URLs?
				);
		  
		return chart; 
	} 
	
	@SuppressWarnings("unused")
	private void saveChart(JFreeChart chart, String chartPath) {
		 
      try {
          ChartUtils.saveChartAsJPEG(new File(chartPath), chart, 500, 300);
      } catch (IOException e) {
          System.err.println("Problem occurred creating chart.");
      }
	}
	
}
