package br.ufpb.ppm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Ppm {

	private static int numeroDeContextos = 3;
	private static int tamanhoDoGrupoDeBits = 8;
	
	private static final int ARGUMENTO_NUMERO_DE_CONTEXTOS = 1;
	private static final int ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS = 2;
	
	/**
	 * 
	 * O programa deve ser chamado passando-se parâmetros.<br/>
	 * Uso: Ppm <i>arquivo</i> <i>[nº de contextos]</i> 
	 * <i>[tamanho do grupo de bits]</i>
	 * 
	 * @param args  
	 * 
	 */
	public static void main(String[] args) {
		
	if (args.length < 1 || args.length > 3) {
		System.out.println("Uso: Ppm arquivo nº de contextos] [tamanho do grupo de bits]");
		System.out.println(" ou: Ppm arquivo [nº de contextos]");
		System.out.println(" ou: Ppm arquivo");
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
	
	if (args.length >= ARGUMENTO_NUMERO_DE_CONTEXTOS+1) {
		int aux = Integer.parseInt(args[ARGUMENTO_NUMERO_DE_CONTEXTOS]);
		if (aux >= 0)
			numeroDeContextos = aux;
	}
	
	if (args.length == ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS+1) {
		int aux = Integer.parseInt(args[ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS]);
		if ((aux%2)==0)
			tamanhoDoGrupoDeBits = aux;
	}
		
		
	byte[] dataBlock = new byte[1024];
	try {
		int numBytes;
		
		while((numBytes = fis.read(dataBlock)) != -1) {
			
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		
	}
	
	/** 
	 * 
	 * Retorna o código binário de um byte
	 * @param byteToConvert
	 * @return String Representação do código binário em String
	 */
	public static String getCode(byte byteToConvert) {
		int aux = 16 * ((byteToConvert & 0xf0) >> 4) + (byteToConvert & 0x0f);
		return getCodeUnsigenedInt(aux);
	}
	
	
	/**
	 * 
	 * Retorna uma String relativa ao código binário de um inteiro sem sinal
	 * @param inteiro Número decimal positivo a ser convertido em binário representado 
	 * como String
	 * @return A String que representa o binário
	 * 
	 */
	public static String getCodeUnsigenedInt(int inteiroPositivo) {
		int resto = inteiroPositivo%2;
		int quosc = inteiroPositivo/2;
		String result = Integer.toString(resto);
		return inteiroPositivo==0?"":getCodeUnsigenedInt(quosc)+result;
	}
}
