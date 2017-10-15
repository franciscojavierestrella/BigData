package BigData.Utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public final class Configuracion  implements Serializable{
	private static final long serialVersionUID = 5995677447005336118L;
	private static Properties prop = null;
	private static Configuracion miConfiguracion = null;
	
	/**
     * Incializa el patron Singleton
     *
     */	
	public  static Configuracion getConfiguracion() {
		 
		 if (miConfiguracion==null) {		 
			 miConfiguracion=new Configuracion();
		 }
		 return miConfiguracion;
	}	
	
	/**
     * Metodo encargado de leer el fichero de configuracion.
     *
     */	
	public static void leerConfiguracio(String configuracion){
		Configuracion.getConfiguracion();
		Configuracion.prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(configuracion);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			prop = null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					prop = null;
				}
			}
		}
		
	}
	
	/**
     * Devuelve el valor para una clave.
     *
     */
	public static String getValue(String key){	
		String valor = prop.getProperty(key);
		return valor; 	
	}
	/**
     * Establece el valor para una clave.
     *
     */
	public static void setValue(String key, String value){
		prop.setProperty(key, value);
	}


}
