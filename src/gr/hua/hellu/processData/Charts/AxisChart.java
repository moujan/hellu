package gr.hua.hellu.processData.Charts;

import java.awt.Paint;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class AxisChart extends JFrame {

    DefaultCategoryDataset result;
    public AxisChart(String applicationTitle, String charTitle, int[][] data, String []bars, String TYPE) {
        super(applicationTitle);

        DefaultCategoryDataset dataset = createDataset(data, bars);

        JFreeChart chart = createChart(dataset, charTitle, TYPE);

        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset(int[][] res, String []bars) {
        result = new DefaultCategoryDataset();
        for (int i = 0; i < 100; i++) {
            if (res[0][i] == 0) {
                for (int bar = 0; bar < bars.length; bar++ ){
                    if (res[bar+1][i] == -1){
                        result.addValue( 0, bars[bar], "Unknown year");
                    }
                    else
                        result.addValue( res[bar+1][i], bars[bar], "Unknown year");
                }
            } else if ( res[0][i] != -1 ){
                for (int bar = 0; bar < bars.length; bar++ ){
                    result.addValue(res[bar+1][i], bars[bar], res[0][i] + "");                
                }
            }
        }
        return result;
    }

    private JFreeChart createChart(DefaultCategoryDataset dataset, String charTitle, String TYPE) {
        JFreeChart chart = ChartFactory.createBarChart3D(charTitle,
                "Year", TYPE, dataset, PlotOrientation.VERTICAL, true, true, false);
         CategoryPlot plot = chart.getCategoryPlot();
         //plot.setRenderer(new MyRender());
         plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
         NumberAxis domainAxis =(NumberAxis) plot.getRangeAxis();
         domainAxis.setAutoRange(true);
         domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }
    
    class MyRender extends BarRenderer {

        @Override
        public Paint getItemPaint(int row, int col) {
            System.out.println(row + " " + col + " " + super.getItemPaint(row, col));
            return super.getItemPaint(row, col);
        }
    }
}

