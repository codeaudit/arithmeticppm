package br.ufpb.ppm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.colloquial.arithcode.ArithDecoder;
import com.colloquial.arithcode.BitInput;

public class PpmDec {
	private static int maiorContexto;
	private static int tamanhoDoGrupoDeBits;

	private static String arquivoDeSaida;
	private static String arquivosDeEntrada[];

	//posicao dos argumentos do programa
	private static final int POSICAO_ARGUMENTO_MAIOR_CONTEXTO = 1;
	private static final int POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS = 2;
	private static final int POSICAO_ARGUMENTO_SAIDA = 3;

	private static Trie arvores[];
	private static String palavraAtual[];
	private static ArithDecoder decodificador[];
	private static FileInputStream fis[];
	//private static File file[];
	private static FileOutputStream fos;
	private static Vector<Vector<Character>> valoresDecodificados;
	private static int totalContextoMenosUm[];
	private static char caracteres[];

	/**
	 * 
	 * O programa deve ser chamado passando-se parâmetros que foram utilizados na codificação.<br/>
	 * Uso: PpmDec <i>arquivo</i> <i>maior contexto</i> <i>tamanho do grupo de bits</i> <i>[nome do arquivo de saída (sem extensão)]</i>
	 * 
	 * @param args  
	 * 
	 */
	public static void main(String[] args) {
		int aux;

		if (args.length < 3 || args.length > 4) {
			System.out.println("Uso: PpmDec arquivo(nome do primeiro arquivo caso sejam vários) maior_contexto tamanho do grupo de bits [nome do arquivo de saída (sem extensão)]");
			System.exit(0);
		}

		aux = Integer.parseInt(args[POSICAO_ARGUMENTO_MAIOR_CONTEXTO]);
		if (aux >= 0 && aux < 128) {
			maiorContexto = aux;
		} else {
			System.err.println("Contexto inválido.");
			System.exit(0);
		}

		aux = Integer.parseInt(args[POSICAO_ARGUMENTO_TAMANHO_DO_GRUPO_DE_BITS]);
		if (aux == 1 || aux == 2 || aux == 4 || aux == 8 || aux == 16)
			tamanhoDoGrupoDeBits = aux;
		else {
			System.err.println("Número inválido para o tamanho do grupo de bits");
			System.err.println("Números válidos: 1, 2, 4, 8 ou 16");
			System.exit(0);
		}

		if (args.length == POSICAO_ARGUMENTO_SAIDA+1) {
			arquivoDeSaida = args[POSICAO_ARGUMENTO_SAIDA];
		} else {
			aux = args[0].lastIndexOf('.');
			arquivoDeSaida = (aux != -1 ) ? 
					args[0].substring(0, aux) + "dec.txt" :
						args[0] + "dec.txt";
		}

		File saida = new File (arquivoDeSaida);
		try {
			fos = new FileOutputStream (saida);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Problema na abertura do arquivo de saída.");
			System.exit(0);
		}

		int numeroDeGruposDeBits = (tamanhoDoGrupoDeBits) < 8 ? 8/tamanhoDoGrupoDeBits : 1;

		arvores = new Trie[numeroDeGruposDeBits];
		palavraAtual = new String[numeroDeGruposDeBits];
		decodificador = new ArithDecoder[numeroDeGruposDeBits];
		fis = new FileInputStream[numeroDeGruposDeBits];
		valoresDecodificados = new Vector<Vector<Character>> ();
		totalContextoMenosUm = new int[numeroDeGruposDeBits];
		caracteres = new char[numeroDeGruposDeBits];
		arquivosDeEntrada = new String[numeroDeGruposDeBits];
		arquivosDeEntrada[0] = args[0];
		long tempoAntes, tempoDepois;

		for (int i = 0; i < numeroDeGruposDeBits; i++) {
			arvores[i] = new Trie(tamanhoDoGrupoDeBits);
			palavraAtual[i] = "";
			totalContextoMenosUm[i] = (int) Math.pow (2, tamanhoDoGrupoDeBits);

			if (numeroDeGruposDeBits > 1) {
				valoresDecodificados.add(new Vector<Character> (totalContextoMenosUm[i]));
			} else if (tamanhoDoGrupoDeBits == 8){
				valoresDecodificados.add(new Vector<Character> (128, 128));
			} else {
				valoresDecodificados.add(new Vector<Character> (512, 512));
			}

			if (i > 0) {
				int indiceZero = args[0].lastIndexOf('0');
				if (indiceZero == -1) {
					System.out.println("Problema na abertura dos arquivos.");
					System.exit(0);
				}
				arquivosDeEntrada[i] = args[0].substring(0, indiceZero) + i + 
				args[0].substring(indiceZero+1);
			}

			//System.out.println("Arquivo de entrada: " + arquivosDeEntrada[i]);

			try {
				fis[i] = new FileInputStream(arquivosDeEntrada[i]);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.err.println("Problema na abertura dos arquivos de entrada.");
				System.exit(0);
			}

			try {
				decodificador[i] = new ArithDecoder(new BitInput(fis[i]));
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Problema na decodificação.");
				System.exit(0);
			}
		}

		tempoAntes = System.currentTimeMillis();
		System.out.println("Iniciando decodificação...");

		int retorno;
		char ch = 0;
		Vector<Character> decodificados;
		byte byteAux;
		int lht[] = new int[3];
		Vector <PseudoNo> contextos;

		while (!decodificador[0].endOfStream()) { // o final da stream de todos os decodificadores ocorrera no mesmo momento
			for (int i = 0; i < numeroDeGruposDeBits; i++) {
				//System.out.println(palavraAtual[i]);
				/*if (palavraAtual[i].length() == 0) {
					ch = decodificaContextoMenosUm(i);
					
					if (palavraAtual[i].length() <= maiorContexto) {
						palavraAtual[i] += ch;
					} else {
						palavraAtual[i] = (maiorContexto > 0) ? palavraAtual[i].substring(1) + ch : "";
					}
					
					//TODO: retornar um PseudoNo aqui para evitar uma segunda busca
					if (!arvores[i].procura(palavraAtual[i]))
						arvores[i].insere(palavraAtual[i]);
					
					continue;
				}*/
				
				//System.out.println(palavraAtual[i]);
				
				contextos = null;
				contextos = arvores[i].retornaContextos(palavraAtual[i], contextos);
				if (contextos == null) {
					System.err.println("Problema na decodificação.");
					System.exit(0);
				}
				
				for (int j = 0; j < contextos.size(); j++) {
					Node contextoAtual = contextos.get(j).no;
					if (contextoAtual.totalDeFilhos == 0) {
						if (j == contextos.size() - 1) // nao ha o contexto atual em k == 0
							ch = decodificaContextoMenosUm(i);
						continue;
					}
					
					retorno = decodificador[i].getCurrentSymbolCount(contextoAtual.totalDeFilhos+contextoAtual.totalEscape);
					if (retorno >= contextoAtual.totalDeFilhos) { // escape
						lht[0] = contextoAtual.totalDeFilhos;
						lht[1] = lht[2] = contextoAtual.totalDeFilhos + contextoAtual.totalEscape;
						try {
							System.out.println("Decodificando: ");
							TestTrie.mostra(lht);
							decodificador[i].removeSymbolFromStream(lht);
						} catch (IOException e) {
							e.printStackTrace();
							System.err.println("Problema na decodificação.");
							System.exit(0);
						}
						if (j == contextos.size() - 1)  { // escape em k == 0
							ch = decodificaContextoMenosUm(i);
						}
					} else {
						lht = arvores[i].retornaNoPeloLow(contextoAtual, retorno, caracteres, i);
						
						ch = caracteres[i];
						try {
							System.out.println("Decodificando: ");
							TestTrie.mostra(lht);
							decodificador[i].removeSymbolFromStream(lht);
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("Problema na decodificação.");
							System.exit(0);
						}
						break;
						//ch = arvores[i].retornaSimbolo (retorno);
					}
				}
				
				for (int j = 0; j < contextos.size(); j++) {
					arvores[i].buscaEInsereEmNo(contextos.get(j).no, ch);
					//arvores[i].percorre();
				}
				
				if (maiorContexto > 0 && palavraAtual[i].length() < maiorContexto) {
					palavraAtual[i] += ch;
				} else {
					palavraAtual[i] = (maiorContexto > 0) ? palavraAtual[i].substring(1) + ch : "";
				}
				
			}
			
			if (tamanhoDoGrupoDeBits > 8) {
				escreveDoisBytes(caracteres[0], fos);
			} else {
				byteAux = juntaArray(caracteres);
				try {
					fos.write(byteAux);
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Problema na escrita.");
					System.exit(0);
				}
			}
			//System.exit(0);
		}
		
		tempoDepois = System.currentTimeMillis();
		System.out.println("Decodificação concluída em: " + (tempoDepois - tempoAntes) / 1000.0 + "s");
	}
	
	public static char decodificaContextoMenosUm (int i) {
		Vector <Character> decodificados = valoresDecodificados.get(i);
		int retorno = decodificador[i].getCurrentSymbolCount(totalContextoMenosUm[i]);
		System.out.println(retorno);
		char ch = encontraSimboloMenosUm(retorno, decodificados);
		decodificados.add(ch);
		//palavraAtual[i] += ch;
		
		//TODO: retornar um PseudoNo aqui para evitar uma segunda busca
		//if (!arvores[i].procura())
		//	arvores[i].insere(palavraAtual[i]);

		caracteres[i] = ch;
		int lht [] = new int[3];
		lht[0] = retorno;
		lht[1] = retorno + 1;
		lht[2] = totalContextoMenosUm[i]--;
		//System.out.println(lht[0]);
		try {
			System.out.println("Decodificando: ");
			TestTrie.mostra(lht);
			decodificador[i].removeSymbolFromStream(lht);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problema na decodificação.");
			System.exit(0);
		}
		return ch;
	}

	public static char encontraSimboloMenosUm (int simbolo, Vector<Character> vetorDecodificados) {
		char aux = (char) simbolo;

		for (int i = 0; i < vetorDecodificados.size(); i++) {
			if (vetorDecodificados.get(i).charValue() <= aux)
				aux++;
		}
		return aux;
	}
	
	public static void escreveDoisBytes (char ch, FileOutputStream fos) {
		try {
			int aux = ch;
			aux = aux & 0xFF;
			fos.write(aux);
			aux = ch;
			aux = aux >> 8;
			fos.write(aux);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problema na escrita.");
			System.exit(0);
		}
	}
	
	public static byte juntaArray (char caracteres[]) {
		if (caracteres.length == 1)
			return (byte) caracteres[0];
		
		byte aux = 0;
		byte aux2;
		int tamanho = caracteres.length;
		int passo;
		if (tamanho == 2)
			passo = 4;
		else if (tamanho == 4)
			passo = 2;
		else if (tamanho == 8)
			passo = 1;
		else {
			return 0; // nao vai acontecer!
		}
		
		for (int i = 0, j = tamanho - 1; i < tamanho; i++, j--) {
			aux2 = (byte) caracteres[i];
			aux += aux2 << (j * passo);
		}
		return aux;
	}
}
