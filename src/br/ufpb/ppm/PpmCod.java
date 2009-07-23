package br.ufpb.ppm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PpmCod {

	private static int maiorContexto = 3; //contexto default
	private static int tamanhoDoGrupoDeBits = 8; //tamanho default
	
	//posicao dos argumentos do programa
	private static final int POSICAO_ARGUMENTO_MAIOR_CONTEXTO = 1;
	private static final int POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS = 2;
	
	private static Trie arvores[];
	private static String palavraAtual[];
	
	/**
	 * 
	 * O programa deve ser chamado passando-se parâmetros.<br/>
	 * Uso: PpmCod <i>arquivo</i> <i>[maior contexto]</i> <i>[tamanho do grupo de bits]</i>
	 * 
	 * @param args  
	 * 
	 */
	public static void main(String[] args) {
		
		if (args.length < 1 || args.length > 3) {
			System.out.println("Uso: PpmCod arquivo maior_contexto [tamanho do grupo de bits]");
			System.out.println(" ou: PpmCod arquivo [maior contexto]");
			System.out.println(" ou: PpmCod arquivo");
			System.exit(0);
		}
			
		File file = new File(args[0]);
		FileInputStream fis = null;
		//FileOutputStream testeEscrita = null;
		try {
			fis = new FileInputStream(file.getCanonicalPath());
			//testeEscrita = new FileOutputStream("testeEscrita.txt");
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo não encontrado");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (args.length >= POSICAO_ARGUMENTO_MAIOR_CONTEXTO+1) {
			int aux = Integer.parseInt(args[POSICAO_ARGUMENTO_MAIOR_CONTEXTO]);
			if (aux >= 0) {
				maiorContexto = aux;
			} else {
				System.err.print("Contexto inválido. Utilizando contexto padrão [");
				System.err.println(maiorContexto + "]");
			}
		}
	
		if (args.length == POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS+1) {
			int aux = Integer.parseInt(args[POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS]);
			if (aux == 1 || aux == 2 || aux == 4 || aux == 8 || aux == 16)
				tamanhoDoGrupoDeBits = aux;
			else {
				System.err.println("Número inválido para o tamanho do grupo de bits");
				System.err.println("Números válidos: 1, 2, 4, 8 ou 16");
				System.exit(0);
			}
		}
	
		int numeroDeGruposDeBits = (tamanhoDoGrupoDeBits) < 8 ? 8/tamanhoDoGrupoDeBits : 1;
		
		arvores = new Trie[numeroDeGruposDeBits];
		palavraAtual = new String[numeroDeGruposDeBits];
		for (int i = 0; i < numeroDeGruposDeBits; i++) {
			arvores[i] = new Trie(tamanhoDoGrupoDeBits);
			palavraAtual[i] = "";
		}
		
		byte[] dataBlock = new byte[1024];
		byte [] bits;
		char chAux;
		int bytesLidos = 0;
		
		//fis.read
		try {
			int numBytes;
			
			while((numBytes = fis.read(dataBlock)) != -1) {
				for (int i=0; i < numBytes; i++) {
					//String[] bits = splitCode(getCode(dataBlock[i]), tamanhoDoGrupoDeBits);
					bits = splitCode(dataBlock[i], tamanhoDoGrupoDeBits);
					bytesLidos++;
					for (int j = 0; j < bits.length; j++) {
						//System.out.println("Valor atual: " + bits[j]);
						
						// se o valor do byte for negativo, faz com que fique com o valor positivo,
						// necessario pois as posicoes do array em Trie sao todas positivas
						chAux = (char) ((bits[j]) >= 0 ? bits[j] : 256 + bits[j]);
						
						//System.out.println("Char: " + (int) chAux);
						
						if ((tamanhoDoGrupoDeBits == 16) && (++i < numBytes)) {
							int aux = (dataBlock[i] >= 0) ? (dataBlock[i] << 8) | chAux
							                              : ((256 + dataBlock[i]) << 8) | chAux; 
							chAux = (char) aux;
							bytesLidos++;
						}
						
						/*System.out.println("Char: " + (int) dataBlock[i]);
						System.out.println("Char: " +(int) chAux);
						
						int teste = (int) chAux & 0xFF;						
						System.out.println("Char: " + (char) teste);
						teste = (int) (int) chAux >> 8;
						System.out.println("Char: " + (char) teste);*/
						
						//	TODO: codificar o símbolo pelo aritmetico antes de atualizar o modelo
						
						if (palavraAtual[j].length() <= maiorContexto) {
							palavraAtual[j] += chAux;
						} else {
							palavraAtual[j] = palavraAtual[j].substring(1) + chAux;
						}
						
						if (palavraAtual[j].length() == 0) continue;
						
						if (!arvores[j].procura(palavraAtual[j]))
							arvores[j].insere(palavraAtual[j]);
						
						for(int k = 0; k < palavraAtual[j].length()-1; k++) {
							if (!arvores[j].procura(palavraAtual[j].substring(k+1))) // se for encontrado, já aumenta o contador
								arvores[j].insere(palavraAtual[j].substring(k+1)); // insere com contador 1 no contexto atual
						}
						
						//System.out.println("Palavra atual: " + palavraAtual[j]);
						
						//testeEscrita.write(bits);
						//ppm(bits[j], contextos.get(j), maiorContexto);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * 
	 * Divide o código em grupos iguais do tamanho do argumento <i>tamanhoDoGrupo</i>
	 * @param code Código a ser dividido
	 * @param tamanhoDoGrupo Tamanho dos grupos de bits resultantes da divisão
	 * @return Um array de inteiros contendo os bits divididos.<br />
	 * Os bits mais significativos ficam nas posições menores<br />
	 * Os bits menos significativos ficam nas posições maiores. 
	 * 
	 */
	public static byte[] splitCode(byte code, int tamanhoDoGrupo) {
		int numeroDeGrupos = (tamanhoDoGrupo < 8) ? 8/tamanhoDoGrupo : 1;
		if (numeroDeGrupos == 1) return new byte[] {code};
		
		byte [] result = new byte[numeroDeGrupos];
		// anula para 8 grupos: 0x01, para 4: 0x03, para 2: 0x0F
		byte anula = (byte) ((tamanhoDoGrupo == 1) ? 1 : tamanhoDoGrupo * tamanhoDoGrupo - 1);
		
		//System.out.println("Lido: " + code);
		
		for (int i = 0, j = numeroDeGrupos-1; i < numeroDeGrupos; i++, j--) {
			result[j] = (byte) ((code >> (i * tamanhoDoGrupo)) & anula);
			//System.out.println("Dividido: " + result[j]);
		}
		return result;
	}
	
}
