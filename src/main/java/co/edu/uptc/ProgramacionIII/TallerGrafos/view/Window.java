package co.edu.uptc.ProgramacionIII.TallerGrafos.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.ProgramacionIII.TallerGrafos.model.Map;
/**
 * La clase de la ventana
 * @author JUAN DIEGO
 *
 */
public class Window extends JFrame{
	private JPanel options;
	private JLabel labelSource;
	private JLabel labelDestination;
	private JComboBox<String> comboSource;
	private JComboBox<String> comboDestination;
	private JButton routeButton;
	private JButton exit;
	private String source;
	private String destination;
	private List<List<Integer>> rutas;
	private Map map = new Map();
	private ArrayList<Double> distancias;
	private ArrayList<ArrayList<String>> rutasEstaciones;
	/**
	 * Contructor de la clase Window
	 */
	public Window() {
		setTitle("Ventana principal");
		setSize(800, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
	}
	/**
	 * Inicia los componentes de la ventana
	 */
	public void initComponents() {
		labelSource = new JLabel("Estacion origen");
		labelDestination = new JLabel("Estacion destino");
		routeButton = new JButton("Rutas");
		exit = new JButton("Salir");
		comboSource = new JComboBox<String>();
		comboSource.addItem("Terminal");
		comboSource.addItem("Colegio");
		comboSource.addItem("Cementerio");
		comboSource.addItem("Hospital");
		comboSource.addItem("Parque");
		comboDestination = new JComboBox<String>();
		comboDestination.addItem("Terminal");
		comboDestination.addItem("Colegio");
		comboDestination.addItem("Cementerio");
		comboDestination.addItem("Hospital");
		comboDestination.addItem("Parque");
		
		options = new JPanel(new GridLayout(3, 2, 20, 20));
		options.add(labelSource);
		options.add(comboSource);
		options.add(labelDestination);
		options.add(comboDestination);
		options.add(routeButton);
		options.add(exit);
		this.add(options);
		
		routeButton.addActionListener(new ActionListener() {
			// Metodo para encontrar las rutas
			public void actionPerformed(ActionEvent e) {
				source = (String) comboSource.getSelectedItem();
				destination = (String) comboDestination.getSelectedItem();
				rutas = map.findWholeRoutes(source, destination);
				distancias = map.calculateDistances(rutas);
				int numRoutes = rutas.size();
				int numEstaciones = 5;
				String[] estaciones = new String[numEstaciones];
				int[] estaciones2 = new int[numEstaciones];
				estaciones[0] = "Terminal";
				estaciones[1] = "Colegio";
				estaciones[2] = "Cementerio";
				estaciones[3] = "Hospital";
				estaciones[4] = "Parque";
				estaciones2[0] = 0;
				estaciones2[1] = 1;
				estaciones2[2] = 2;
				estaciones2[3] = 3;
				estaciones2[4] = 4;
				rutasEstaciones = convertRutas(estaciones, estaciones2);
				
				String mensaje = map.distanceShortest(distancias, rutasEstaciones);
				JOptionPane.showMessageDialog(null, mensaje);
				
				Object[] options = showRoutas(rutasEstaciones, distancias);
				String opcionSeleccionada = (String) JOptionPane.showInputDialog(null, "Seleccione una opción", "Título del cuadro de diálogo", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		        JOptionPane.showMessageDialog(null, "Seleciono la opcion: "+opcionSeleccionada);
			}
		});
	}
	/**
	 * Retorna una lista con las posibles rutas y el nombre de sus estaciones
	 * @param estaciones
	 * @param estaciones2
	 * @return rutasEstaciones
	 */
	public ArrayList<ArrayList<String>> convertRutas(String[] estaciones, int[] estaciones2){
		ArrayList<ArrayList<String>> rutasEstaciones = new ArrayList<ArrayList<String>>();
		int numRuta = 0;
		for (List<Integer> ruta : rutas) {
			int estacion = 0;
			ArrayList<String> arrayList = new ArrayList<String>();
			for (int i = 0; i < ruta.size(); i++) {
				estacion = ruta.get(i);
				boolean centinela = true;
				for (int j = 0; j < estaciones2.length && centinela==true; j++) {
					if(estacion == estaciones2[j]) {
						arrayList.add(i, estaciones[j]);
						centinela = false;
					}
				}
			}
			rutasEstaciones.add(arrayList);
			numRuta++;
		}
		return rutasEstaciones;
	}
	/**
	 * Retorna los objetos que se muestran en un combobox
	 * @param rutasEstaciones
	 * @param distancias
	 * @return optionsRutas
	 */
	public Object[] showRoutas(ArrayList<ArrayList<String>> rutasEstaciones, ArrayList<Double> distancias) {
		int size = rutasEstaciones.size();
		Object[] optionsRutas = new Object[size];
		String[] estaciones = new String[size];
		int contador = 0;
		for (ArrayList<String> ruta : rutasEstaciones) {
			String cadena = "Ruta "+contador+": ";
			for (int i = 0; i < ruta.size(); i++) {
				cadena = cadena+ruta.get(i)+", ";
			}
			cadena = cadena +"distancia:"+distancias.get(contador);
			optionsRutas[contador] = cadena;
			contador++;
		}
		return optionsRutas;
	}
}
