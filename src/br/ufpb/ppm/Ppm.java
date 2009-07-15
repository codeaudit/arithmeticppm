package br.ufpb.ppm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Ppm {

	/**
	 * 
	 * O programa deve ser chamado passando-se parâmetros.<br/>
	 * Uso: Ppm <i>arquivo</i> <i>[nº de contextos]</i>
	 * 
	 * @param args  
	 * 
	 */
	public static void main(String[] args) {
		
	if (args.length < 1 || args.length > 2) {
		System.out.println("Uso: Ppm arquivo [nº de contextos]");
		System.exit(0);
	}
		
	File file = new File(args[0]);
	FileInputStream fis = null;
	try {
		fis = new FileInputStream(file.getCanonicalPath());
	} catch (FileNotFoundException e) {
		System.out.println("Arquivo não encontrado");
		System.exit(0);
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	byte[] block = new byte[1024];
	try {
		fis.read(block);
	} catch (IOException e) {
		e.printStackTrace();
	}
		
	}
	
}
