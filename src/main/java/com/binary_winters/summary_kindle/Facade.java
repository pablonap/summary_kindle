package com.binary_winters.summary_kindle;


import java.io.File;

import com.binary_winters.summary_kindle.exceptions.PathErroneoONoHalladoException;

public class Facade {
	
	private Batch batch;
	private static final String RUTA_PREDETERMINADA_DE_CLIPPINGS_DENTRO_DEL_DEVICE = "documents/My Clippings.txt";
	
	public Facade( Batch batch ){
		this.batch = batch;
	}
	
	public void armarResumen( String pathAbsolutoDeClippings ) throws PathErroneoONoHalladoException{
		batch.armarMapaConResumenPorTitulo( pathAbsolutoDeClippings );
		batch.crearDirectorioParaResumenes();
		batch.generarResumenesConMapa();
	}

	public Boolean validarPahtPredeterminadoPorKindleDeClippings( String path ) {
		File pahtAbsolutoDeClippings = new File( path + RUTA_PREDETERMINADA_DE_CLIPPINGS_DENTRO_DEL_DEVICE );
		
		Boolean respuesta;
		
		if( pahtAbsolutoDeClippings.exists() ){
			respuesta = Boolean.TRUE;
		} else{
			respuesta = Boolean.FALSE;
		}
		
		return respuesta;
	}

	public String verificarBarraFinalDePath(String rutaDevice) {
		if( String.valueOf( rutaDevice.charAt( rutaDevice.length() - 1 ) ).equals("/") == false ){
			rutaDevice = rutaDevice + "/";
		}
		
		return rutaDevice;
	}
}
