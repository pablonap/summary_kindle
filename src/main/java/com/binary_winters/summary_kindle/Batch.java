package com.binary_winters.summary_kindle;

import com.binary_winters.summary_kindle.exceptions.*;

public interface Batch {
	public void armarMapaConResumenPorTitulo( String pathAbsolutoDeClippings ) throws PathErroneoONoHalladoException;
	
	// Por cada titulo del mapa se creará un archivo con su correspondiente resumen.
	public void generarResumenesConMapa();
	
	public void crearDirectorioParaResumenes() throws PathErroneoONoHalladoException;
}
