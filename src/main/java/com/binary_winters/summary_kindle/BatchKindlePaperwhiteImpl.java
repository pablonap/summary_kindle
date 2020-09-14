package com.binary_winters.summary_kindle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.binary_winters.summary_kindle.exceptions.PathErroneoONoHalladoException;


public class BatchKindlePaperwhiteImpl implements Batch {
	
	private File resumen_kindle;
	private FileReader reader;
	private Map<String, List<String>> biblioteca;
	private String rutaDeDirectorioParaResumenes;
	
	private static final String EXTENSION_DE_LIBRO_RESUMIDO = ".txt";
	private static final String SEPARADOR = "=====";
	
//	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle( "mensajesDeError" );  
	
	private static final String MSJ_ERROR_PARA_RESUMEN_NO_ENCONTRADO = "Summary not found";
	private static final String MSJ_ERROR_PARA_ARCHIVO_INCORRECTO = "Wrong file";
	

	public void armarMapaConResumenPorTitulo(String pathAbsolutoDeClippings) throws PathErroneoONoHalladoException {
		resumen_kindle = new File( pathAbsolutoDeClippings );
		
		if( resumen_kindle.exists() == false ){
			throw new PathErroneoONoHalladoException( MSJ_ERROR_PARA_RESUMEN_NO_ENCONTRADO );
		}
		
		String lineaDeArchivo = null;
		String lineaAnteriorDeArchivo = null;
		try {
			
			if( resumen_kindle.isFile() == false ){
				throw new PathErroneoONoHalladoException( MSJ_ERROR_PARA_ARCHIVO_INCORRECTO );
			}
			
			reader = new FileReader( resumen_kindle );
			
			BufferedReader lineReader = new BufferedReader( reader );
			lineaDeArchivo = lineReader.readLine();
			
			biblioteca = new HashMap<String, List<String>>();
			
			List<String> notasDeLibro;
			
			String tituloActual = null;
			
			// Esta bandera se debe a que hay una regla que se cumple que todo título siempre está por debajo de 
			// los separadores "===" excepto para el primer renglón del My Clipping donde no hay separador y directamente
			// está el título.
			Boolean primeraVuelta = Boolean.FALSE;
			
			while( lineaDeArchivo != null ){
				
				// Si se cumple esto, entonces la línea corresponde a un título.
				if( lineaDeArchivo.contains( SEPARADOR ) == false && 
						( ( lineaAnteriorDeArchivo != null &&
						lineaAnteriorDeArchivo.contains( SEPARADOR ) ) || primeraVuelta == false ) 
						 ){
					
					// Dado que algunos títulos traen como primer char un char basura (que no es ni num ni alf.)
					if( Character.isDigit( lineaDeArchivo.charAt(0) ) == false && 
							Character.isLetter( lineaDeArchivo.charAt(0) ) == false ){
						lineaDeArchivo = lineaDeArchivo.substring( 1, lineaDeArchivo.length() );
					}
					
					// Si el titulo no estaba en la biblioteca...
					if( biblioteca.containsKey( lineaDeArchivo ) == false ){
						notasDeLibro = new ArrayList<String>();
						biblioteca.put( lineaDeArchivo , notasDeLibro );
						
					}
					
					tituloActual = lineaDeArchivo;
				} else{
					// Si se cumple que no empieza con "=" o "-" o espacio... 
					if( lineaDeArchivo.isEmpty() == false
							&& lineaDeArchivo.substring(0, 1).equals( "=" ) == false &&
							lineaDeArchivo.substring(0, 1).equals( "-" ) == false &&
							lineaDeArchivo.substring(0, 1).equals( " " ) == false){
						
						notasDeLibro = biblioteca.get( tituloActual );
						notasDeLibro.add( lineaDeArchivo );
						
						biblioteca.put( tituloActual , notasDeLibro );
					}
				}
				
				lineaAnteriorDeArchivo = lineaDeArchivo;
				lineaDeArchivo = lineReader.readLine();
				
				primeraVuelta = Boolean.TRUE;
			}
		} catch (FileNotFoundException e) {
			// loguear
		} catch (IOException e) {
			// loguear
		}
	}

	public void generarResumenesConMapa() {
		File fileOutPut;
		FileWriter writer = null;
		BufferedWriter archivoDeLibro = null;
		List<String> parrafosDelResumenDelLibro;
		
		Iterator<String> it = biblioteca.keySet().iterator();
		
		while(it.hasNext()){
			String tituloDeLibro = it.next();
			
			fileOutPut = new File( rutaDeDirectorioParaResumenes + tituloDeLibro + EXTENSION_DE_LIBRO_RESUMIDO  );
		
			try{
				writer = new FileWriter( fileOutPut );
				archivoDeLibro = new BufferedWriter( writer );
				
				parrafosDelResumenDelLibro = biblioteca.get(tituloDeLibro);
				
				for( String parrafo : parrafosDelResumenDelLibro ){
					archivoDeLibro.write( parrafo );
					archivoDeLibro.write("\n\n=====================\n\n");
				}
			} catch( IOException ex ){
				// loguear
			}
			
			finally{
				if( archivoDeLibro != null ){
					try {
						archivoDeLibro.close();
					} catch (IOException e) {
						// loguear
					}
				}
			}
		}
	}

	public void crearDirectorioParaResumenes() throws PathErroneoONoHalladoException {
		System.out.println( "\n Enter full path where you want to save your summaries" );
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner( System.in );
		rutaDeDirectorioParaResumenes = scanner.nextLine();
		
		if( String.valueOf( rutaDeDirectorioParaResumenes.charAt( rutaDeDirectorioParaResumenes.length() - 1 ) ).equals("/") == false ){
			rutaDeDirectorioParaResumenes = rutaDeDirectorioParaResumenes + "/";
		}
		
		File dir = new File( rutaDeDirectorioParaResumenes );
		dir.mkdir();
		
	}
	
}
