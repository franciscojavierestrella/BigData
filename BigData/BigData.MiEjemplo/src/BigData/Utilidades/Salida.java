package BigData.Utilidades;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Salida {
	
	private String path = "";
	private String fichero = "";
	
	public Salida(String path, String fichero){
		this.path = path;
		this.fichero = fichero;		
	}
	
	public Salida(String path){
		this.path = path;
	}
	
	private String getNameFile()
	{
		if ( fichero.isEmpty())
			return path;
		return path + "/" + fichero;
	}
	
	public void AbrirFichero(String texto)
	{
	    //Obtenemos el fichero de salida
	    Path pathOut = Paths.get(getNameFile());
	    
	    //Creamos el fichero al principio---
	    try (BufferedWriter writer = Files.newBufferedWriter(pathOut)) 
	    {
	        writer.write(texto);       
	        writer.newLine();
	    } catch (IOException e) {
			System.exit(0);
		}    	
	}
	
	public void EscribirFichero(String texto)
	{
		//Vocamos a fichero los cluster encontrador.
        try (FileWriter writer = new FileWriter( getNameFile(), true)) 
        {
            writer.write(texto);
            writer.write("\r\n");
        } catch (IOException e) {
    		System.exit(0);
    	} 
	}
	

}
