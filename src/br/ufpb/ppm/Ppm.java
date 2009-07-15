package br.ufpb.ppm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

public class Ppm {

	private static int maiorContexto = 3; //contexto default
	private static int tamanhoDoGrupoDeBits = 8; //tamanho default
	
	//posicao dos argumentos do programa
	private static final int POSICAO_ARGUMENTO_MAIOR_CONTEXTO = 1;
	private static final int POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS = 2;
	
	private static Hashtable[] contextos;
	
	/**
	 * 
	 * O programa deve ser chamado passando-se parâmetros.<br/>
	 * Uso: Ppm <i>arquivo</i> <i>[maior contexto]</i> <i>[tamanho do grupo de bits]</i>
	 * 
	 * @param args  
	 * 
	 */
	public static void main(String[] args) {
		
	if (args.length < 1 || args.length > 3) {
		System.out.println("Uso: Ppm arquivo maior_contexto [tamanho do grupo de bits]");
		System.out.println(" ou: Ppm arquivo [maior contexto]");
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
	
	if (args.length >= POSICAO_ARGUMENTO_MAIOR_CONTEXTO+1) {
		int aux = Integer.parseInt(args[POSICAO_ARGUMENTO_MAIOR_CONTEXTO]);
		if (aux >= 0) {
			maiorContexto = aux;
		}
	}

	if (args.length == POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS+1) {
		int aux = Integer.parseInt(args[POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS]);
		if (aux == 1 || aux == 2 || aux == 4 || aux == 8)
			tamanhoDoGrupoDeBits = aux;
		else {
			System.out.println("Número inválido para o tamanho do grupo de bits");
			System.out.println("Números válidos: 1, 2, 4 ou 8");
			System.exit(0);
		}
	}

	//criando um array de tamanho mínimo == 2, pois existe o contexto 0 e o -1
	contextos = new Hashtable[maiorContexto+2]; 
		
	byte[] dataBlock = new byte[1024];
	try {
		int numBytes;
		
		while((numBytes = fis.read(dataBlock)) != -1) {
			
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		
	}
	
	public static String[] splitCode(String code, int tamanhoDoGrupo) {
		int inicio = 0;
		int fim = tamanhoDoGrupo;
		int numeroDeGrupos = 8/tamanhoDoGrupo;
		String [] result = new String[numeroDeGrupos];
		for (int i = 0; i <= numeroDeGrupos-1; inicio += tamanhoDoGrupo, fim += tamanhoDoGrupo, i++) {
			result[i] = code.substring(inicio, fim);
		}
		return result;
	}
	
	
	/** 
	 * 
	 * Retorna o código binário de um byte
	 * @param byteToConvert
	 * @return String Representação do código binário em String
	 */
	public static String getCode(byte byteToConvert) {
		//caso o byte tenha o primeiro bit == 1, este não será considerado como sinal
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
