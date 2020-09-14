package com.binary_winters.summary_kindle;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.binary_winters.summary_kindle.exceptions.PathErroneoONoHalladoException;
import com.binary_winters.summary_kindle.exceptions.ValorNoNumericoException;

public class SummaryKindleMain {

	private static final String RUTA_PREDETERMINADA_DE_MY_CLIPPINGS = "documents/My Clippings.txt";

//	private static ResourceBundle resourceBundle = ResourceBundle.getBundle( "mensajesDeError" );
	
	private static final String MSJ_DE_ERROR_PARA_DIGITOS = "Only digits.";
	private static final String MSJ_ERROR_PARA_DISPOSITIVO_NO_HALLADO = "Device not found, please try again.";
	private static final String MSJ_ERROR_PARA_RUTA_INCORRECTA = "Directory is not correct.";
	
	public static void main(String[] args) {
		try {
			mostrarMenu();
		} catch( ValorNoNumericoException ex ){
			System.out.println( ex.getMessage() );
		} catch( PathErroneoONoHalladoException ex ){
			System.out.println( ex.getMessage() );
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void mostrarMenu() throws ValorNoNumericoException, PathErroneoONoHalladoException, IOException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner( System.in );
		
		System.out.println( "\n-> SummaryKindle allows you in a just few steps grouping in a file per book all those highlights and notes you've done.");
		System.out.println( "   It works with \"My Clippings\" file built by Kindle where you can find everything mixed.\n" );

		System.out.println("===========================================");
		System.out.println("|              SummaryKindle              |");
		System.out.println("===========================================");
		System.out.println("| Options:                                |");
		System.out.println("|        0. Kindle Paperwhite device      |");
		System.out.println("|        1. Exit program                  |");
		System.out.println("===========================================");

		System.out.print( "\n\nYour choice: " );
		String opcion = scanner.nextLine();
		
		Pattern patron = Pattern.compile("[0-9]+");
	    Matcher matcher = patron.matcher( opcion );
	    
	    if ( matcher.matches() == false ) {
	    	throw new ValorNoNumericoException( MSJ_DE_ERROR_PARA_DIGITOS );
	    }
		
	    Integer opcionNumerica = Integer.parseInt( opcion );
	    
		switch ( opcionNumerica ) {
		case 0:
			mostrarUI( opcionNumerica );
			System.out.println( "\n=======================" );
			System.out.println( "PROCESS SUCCESSFUL" );
			System.out.println( "=======================" );
			break;
		case 1:
			System.out.println( "Bye!" );
			break;
		default:
			System.out.println( "You can choose only between 0 y 1 options." );
			break;
		}
	}
	
	public static void mostrarUI( Integer tipoDispositivo ) throws ValorNoNumericoException, PathErroneoONoHalladoException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		Batch batch = BatchFactory.getBatch( tipoDispositivo );
		
		if( batch == null ){
			throw new PathErroneoONoHalladoException( MSJ_ERROR_PARA_DISPOSITIVO_NO_HALLADO );
		}
		
		Facade facade = new Facade(batch);
		
		System.out.println( "\nEnter the route for your Kindle device (ie: \"/media/richard/Kindle/\") or the route where you move the file built by Kindle: " );
		
		String pathIngresadoPorUsuario = scanner.nextLine();
		
		pathIngresadoPorUsuario = facade.verificarBarraFinalDePath( pathIngresadoPorUsuario );
		
		File filePathDevice = new File(pathIngresadoPorUsuario);
		
		// Puede existir la ruta, tanto como se refiera al dispositivo o como al path ingresado por el usuario por fuera del dispositivo.
		if( filePathDevice.exists() ){
			Boolean existePahtPredeterminadoPorKindleDeClippings = facade.validarPahtPredeterminadoPorKindleDeClippings( pathIngresadoPorUsuario );
			
			if( existePahtPredeterminadoPorKindleDeClippings ){
				facade.armarResumen( pathIngresadoPorUsuario + RUTA_PREDETERMINADA_DE_MY_CLIPPINGS );
			} else{
				System.out.print( "\nEnter a file name summary (the default name by Kindle is \"My Clippings.txt\"): " );
				String nombreFicheroResumen = scanner.nextLine();
				facade.armarResumen( pathIngresadoPorUsuario + nombreFicheroResumen );
			}
			
		} else{
			throw new PathErroneoONoHalladoException( MSJ_ERROR_PARA_RUTA_INCORRECTA ); 
		}
	}
	
}
