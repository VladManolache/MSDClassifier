package utils;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import local.AccuracyCalculating;
import local.TestSetGenreExtracting;
import main.Configurations;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;

import data.genre.BootstrapGenres;
import data.genre.Genres;


public class PieChartResultsVisualiser extends JFrame {
  
	private static final long serialVersionUID = 8023634203110679574L;

	/*
	 *  Visualize the results as a PieChart.
	 *  Input: result_file_name
	 */
	public static void run(String resultsFilePath, Boolean showPredictions) throws Exception { 
		String title = showPredictions? "Predicted genres" : "Actual genres";
		String chartTitle = showPredictions ? "Clasification results" : "Genres";
		PieChartResultsVisualiser demo = new PieChartResultsVisualiser(title, chartTitle, resultsFilePath, showPredictions);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
	
	public PieChartResultsVisualiser(String applicationTitle, String chartTitle, String resultsFile, Boolean showPredictions) throws Exception {
		super(applicationTitle); 
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// This will create the dataset  
		DefaultPieDataset dataset = createDataset(resultsFile, showPredictions);
		 
		// Generate the graph
		JFreeChart chart = createChart(dataset, chartTitle);
		ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow", true)); 
		String result = "Classification accuracy: "+AccuracyCalculating.calculateTotalAccuracy(resultsFile)+"%";
		TextTitle source = new TextTitle(result, new Font("Courier New", Font.PLAIN, 12));
		if (showPredictions) {
			chart.addSubtitle(source);
		}
		
		// we put the chart into a panel
		ChartPanel chartPanel = new ChartPanel(chart);
		
		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		
		// add it to our application
		setContentPane(chartPanel); 
	}
 
 
	private DefaultPieDataset createDataset(String fileName, Boolean showPredictions) throws Exception {
		  
		DefaultPieDataset dataset = new DefaultPieDataset(); 
        String currentClass;
        String currentRow;
        int count; 
        double percentage; 
        
        // Map the class id's from the results file back to initial ids.
		ArrayList<String> classMappings = ResultsManagement.getClassMappings();
		ArrayList<String> correspondingRow;
		if (showPredictions) {
			correspondingRow = ResultsManagement.getFinalResults(fileName, classMappings);
		}
		else {
			ArrayList<Integer> values = TestSetGenreExtracting.run(); 
			correspondingRow = new ArrayList<String>();
			int runConfiguration = Configurations.runConfiguration;
			for (Integer value : values) {
				if (runConfiguration != 1) {
					correspondingRow.add(value + "");
				} else {
					correspondingRow.add(classMappings.get(value));
				}
			} 
		}
        
        // Add the series to your data set. 
        ArrayList<String> classesInResultsFile = ResultsManagement.getClassesInResultsFile(fileName); 
        for(int i = 0; i < classesInResultsFile.size(); i++) {
        	currentClass = classesInResultsFile.get(i);
        	count = 0;
        	for(int j = 0; j < correspondingRow.size(); j++) { 
        		currentRow = correspondingRow.get(j);
        		if(currentRow.equals(currentClass))
        			count++;
        	}
        	percentage = (count*1.0)/10;
        	
        	if(Configurations.bootstrapMSD && Configurations.singleClassLabeling) { 
        		dataset.setValue(BootstrapGenres.valueFromId(i), percentage);
        	}
        	else {  
        		dataset.setValue(Genres.valueFromId(i), percentage);
        	} 
        }  
		return dataset; 
	}

	private JFreeChart createChart(DefaultPieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart(
				title,
//				"Test data",
//				"Class identification",
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
          ChartUtilities.saveChartAsJPEG(new File(chartPath), chart, 500, 300);
      } catch (IOException e) {
          System.err.println("Problem occurred creating chart.");
      }
	}
} 
