package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Nsc_hack
 */
public class Graficas {
    JFreeChart grafica;
    XYSeriesCollection dataset = new XYSeriesCollection();
    String titulo;
    String tx;
    String ty;
    public final static int LINEAL=1;
    public final static int POLAR=2;
    public final static int DISPERSION=3;
    public final static int AREA=4;
    public final static int LOGARITMICA=5;
    public final static int SERIETIEMPO=6;
    public final static int PASO=7;
    public final static int PASOAREA=8;
    
    private static Color COLOR_SERIE_1 = new Color(255, 0, 0);
 
    private static Color COLOR_SERIE_2 = new Color(60, 60, 60);
 
    private static Color COLOR_RECUADROS_GRAFICA = new Color(31, 87, 4);
 
    private static Color COLOR_FONDO_GRAFICA = Color.white;
    
    public Graficas(int tipo,String titulo,String tituloX,String tituloY){
        this.titulo = titulo;
        this.tx = tituloX;
        this.ty = tituloY;
        tipoGrafica(tipo);
        
        // color de fondo de la gráfica
        grafica.setBackgroundPaint(COLOR_FONDO_GRAFICA);
        
 
        final XYPlot plot = (XYPlot) grafica.getPlot();
        configurarPlot(plot);
 
        final NumberAxis domainAxis = (NumberAxis)plot.getDomainAxis();
        configurarDomainAxis(domainAxis);
         
        final NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        configurarRangeAxis(rangeAxis);
        
        //colores, estilos grafica
        final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
        configurarRendered(renderer);
    }
    
    public void tipoGrafica(int tipo){
        switch (tipo){
            case LINEAL :
                grafica = ChartFactory.createXYLineChart(titulo, tx, ty, dataset, PlotOrientation.VERTICAL, true, true, true);
                break;
            case POLAR :
                grafica = ChartFactory.createPolarChart(titulo, dataset, true, true, true);
                break;
            case DISPERSION :
                grafica = ChartFactory.createScatterPlot(titulo, tx, ty, dataset, PlotOrientation.VERTICAL, true, true, true);
                break;
            case AREA :
                grafica = ChartFactory.createXYAreaChart(titulo, tx, ty, dataset, PlotOrientation.VERTICAL, true, true, true);
                break;
            case LOGARITMICA : 
                grafica = ChartFactory.createXYLineChart(titulo, tx, ty, dataset, PlotOrientation.VERTICAL, true, true, true);
                XYPlot ejes = grafica.getXYPlot();
                NumberAxis rango = new LogarithmicAxis(ty);
                ejes.setRangeAxis(rango);
                break;
            case SERIETIEMPO :
                grafica = ChartFactory.createTimeSeriesChart(titulo, tx, ty, dataset, true, true, true);
                break;
            case PASO :
                grafica = ChartFactory.createXYStepChart(titulo, tx, ty, dataset, PlotOrientation.VERTICAL, true, true, true);
                break;
            case PASOAREA :
                grafica = ChartFactory.createXYStepAreaChart(titulo, tx, ty, dataset, PlotOrientation.VERTICAL, true, true, true);
                break;
            default : break;   
        }
        
    }
    
    // configuramos el contenido del gráfico (damos un color a las líneas que sirven de guía)
    private void configurarPlot (XYPlot plot) {
        plot.setDomainGridlinePaint(COLOR_RECUADROS_GRAFICA);
        plot.setRangeGridlinePaint(COLOR_RECUADROS_GRAFICA);
    }
     
    // configuramos el eje X de la gráfica (se muestran números enteros y de uno en uno)
    private void configurarDomainAxis (NumberAxis domainAxis) {
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setTickUnit(new NumberTickUnit(1));
        //domainAxis.setRange(0, 50);
        
    }
     
    // configuramos el eje y de la gráfica (números enteros de dos en dos y rango entre 120 y 135)
    private void configurarRangeAxis (NumberAxis rangeAxis) {
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //rangeAxis.setTickUnit(new NumberTickUnit(2));
        //rangeAxis.setRange(0, 1023);
    }
     
    // configuramos las líneas de las series (añadimos un círculo en los puntos y asignamos el color de cada serie)
    private void configurarRendered (XYLineAndShapeRenderer renderer) {
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShapesVisible(3, true);
        renderer.setSeriesPaint(0, COLOR_SERIE_1);
        renderer.setSeriesPaint(1, COLOR_SERIE_2);
    }
    
    
    public void cargarGrafica(String id, double[] x, double[] y){
        XYSeries s = new XYSeries(id);
        int n = x.length;
        
        for (int i = 0; i < n; i++) {
            s.add(x[i], y[i]);
        }
        dataset.addSeries(s);
        
    }
    
    public ChartPanel obtenerPanel(){
        ChartPanel cp = new ChartPanel(grafica);
        return cp;
    }
    
    public ImageIcon getGraficaImage(Dimension d){
        
        BufferedImage bufferedImage  = grafica.createBufferedImage( d.width, d.height);
        
        ImageIcon ii = new ImageIcon(bufferedImage);
        
        return ii;
    }
    
    public double[][] getArrayGrafica(String id){
        return dataset.getSeries(id).toArray();
    }
    
    public void getArregloX(){
        //dataset.
    }
    
    public String getIDGraficas(int index){
        return (String) dataset.getSeriesKey(index);
    }

    public JFreeChart getGrafica() {
        return grafica;
    }

    public void setGrafica(JFreeChart grafica) {
        this.grafica = grafica;
    }

    public XYSeriesCollection getDataset() {
        return dataset;
    }

    public void setDataset(XYSeriesCollection dataset) {
        this.dataset = dataset;
    }
 
}
