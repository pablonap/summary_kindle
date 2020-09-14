package com.binary_winters.summary_kindle;

public class BatchFactory {
	private static final Integer KINDLE_TIPO_PAPERWHITE = 0;
	
	public static Batch getBatch( Integer tipoBatch ){
		Batch batch = null;
		if( tipoBatch.equals( KINDLE_TIPO_PAPERWHITE ) ){
			batch = new BatchKindlePaperwhiteImpl();
		}
		
		return batch;
	}
}
