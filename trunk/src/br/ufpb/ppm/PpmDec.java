package br.ufpb.ppm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import com.colloquial.arithcode.ArithDecoder;
import com.colloquial.arithcode.BitInput;

public class PpmDec {
	private static int maiorContexto;
	private static int tamanhoDoGrupoDeBits;
	private static int totalBytes;

	private static String arquivoDeSaida;
	private static String arquivosDeEntrada[];
	private static final int TAMANHO_CABECALHO = 6;

	//posicao dos argumentos do programa
	private static final int POSICAO_ARGUMENTO_SAIDA = 1;

	private static Trie arvores[];
	private static String palavraAtual[];
	private static ArithDecoder decodificador[];
	private static FileInputStream fis[];
	//private static File file[];
	private static FileOutputStream fos;
	private static String nomeTemp;
	private static File temporario;
	private static Vector<Vector<Character>> valoresDecodificados;
	private static int totalContextoMenosUm[];
	private static char caracteres[];

	static int parada = 0;

	/**
	 * 
	 * O programa deve ser chamado passando-se parâmetros que foram utilizados na codificação.<br/>
	 * Uso: PpmDec <i>arquivo</i> <i>maior contexto</i> <i>tamanho do grupo de bits</i> <i>[nome do arquivo de saída]</i>
	 * 
	 * @param args  
	 * 
	 */
	public static void main(String[] args) {
		int aux;

		/*if (args.length < 3 || args.length > 4) {
			System.out.println("Uso: PpmDec arquivo(nome do primeiro arquivo caso sejam vários) maior_contexto tamanho do grupo de bits [nome do arquivo de saída (sem extensão)]");
			System.exit(0);
		}*/

		if (args.length < 1 || args.length > 2) {
			System.out.println("Uso: PpmDec arquivo(nome do primeiro arquivo caso sejam vários) [nome do arquivo de saída (sem extensão)]");
			System.exit(0);
		}

		try {
			FileInputStream leitorAux = new FileInputStream (args[0]);
			byte cabecalho[] = new byte[TAMANHO_CABECALHO];
			int lidos;
			lidos = leitorAux.read(cabecalho);
			if (lidos != 6) {
				System.err.println("Problema na leitura.");
				System.exit(0);
			}

			//totalBytes = (int) (cabecalho[0] << 24) | (cabecalho[1] << 16) | (cabecalho[2] << 8) | cabecalho[3];
			totalBytes = 0;
			totalBytes += (cabecalho[0] >= 0) ? cabecalho[0] << 24 : (256 + cabecalho[0]) << 24;
			totalBytes += (cabecalho[1] >= 0) ? cabecalho[1] << 16 : (256 + cabecalho[1]) << 16;
			totalBytes += (cabecalho[2] >= 0) ? cabecalho[2] << 8 : (256 + cabecalho[2]) << 8;
			totalBytes += (cabecalho[3] >= 0) ? cabecalho[3] : 256 + cabecalho[3];
			maiorContexto = cabecalho[4];
			tamanhoDoGrupoDeBits = cabecalho[5];

			aux = args[0].lastIndexOf('.');
			nomeTemp = (aux != -1) ?
					args[0].substring(0, aux) : args[0];

					temporario = File.createTempFile(nomeTemp, ".tmp");
					temporario.deleteOnExit();
					FileOutputStream copiaTemp = new FileOutputStream (temporario);

					copiaConteudo (copiaTemp, leitorAux);

					copiaTemp.close();
					leitorAux.close();

		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			System.err.println("Arquivo não encontrado.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problema na leitura.");
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
		StringBuffer exclusao, exclusaoAux;
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
				if (i == 0)
					fis[i] = new FileInputStream(temporario);
				else
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
		byte byteAux;
		int lht[] = new int[3];
		Vector <PseudoNo> contextos;

		while (totalBytes > 0) { // o final da stream de todos os decodificadores ocorrera no mesmo momento
			totalBytes--;

			for (int i = 0; i < numeroDeGruposDeBits; i++) {
				//System.out.println(palavraAtual[i]);
				exclusao = new StringBuffer();
				
				contextos = null;
				contextos = arvores[i].retornaContextos(palavraAtual[i], contextos);
				if (contextos == null) {
					System.err.println("Problema na decodificação.");
					System.exit(0);
				}

				for (int j = 0; j < contextos.size(); j++) {
					exclusaoAux = new StringBuffer();
					Node contextoAtual = contextos.get(j).no;
					if (contextoAtual.totalDeFilhos == 0) {
						if (j == contextos.size() - 1) // nao ha o contexto atual em k == 0
							ch = decodificaContextoMenosUm(i);
						continue;
					}

					//if (++parada == 100) System.exit(0);
					//++parada;
					
					if (decodificador[i].endOfStream()) continue;
					
					int totalDeFilhosExclusao = arvores[i].getTotalDeFilhosExclusaoEAtualiza(contextoAtual, exclusao, exclusaoAux);
					//System.out.print("Encontrado/normal: " + (totalDeFilhosExclusao+contextoAtual.totalEscape));
					//System.out.println("/"+(contextoAtual.totalDeFilhos+contextoAtual.totalEscape));

					retorno = decodificador[i].getCurrentSymbolCount(totalDeFilhosExclusao+contextoAtual.totalEscape);
					if (retorno >= totalDeFilhosExclusao) { // escape
						lht[0] = totalDeFilhosExclusao;
						lht[1] = lht[2] = totalDeFilhosExclusao + contextoAtual.totalEscape;
						try {
							//System.out.println("Decodificando 1: ");
							//TestTrie.mostra(lht);
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
						//System.out.println(System.currentTimeMillis() - tempoAntes);
						lht = arvores[i].retornaNoPeloLow(contextoAtual, retorno, caracteres, i, exclusao);
						lht[2] = totalDeFilhosExclusao + contextoAtual.totalEscape;
						//System.out.println(System.currentTimeMillis() - tempoAntes);

						ch = caracteres[i];
						try {
							//System.out.println("Decodificando 2 (" +i + "): ");
							//TestTrie.mostra(lht);
							decodificador[i].removeSymbolFromStream(lht);
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("Problema na decodificação.");
							System.exit(0);
						}
						break;
						//ch = arvores[i].retornaSimbolo (retorno);
					}
					//System.out.println("Antes de juntar");
					//System.out.println(System.currentTimeMillis() - tempoAntes);
					arvores[i].juntaStrings(exclusao, exclusaoAux);
					//System.out.println("Depois de juntar");
					//System.out.println(System.currentTimeMillis() - tempoAntes);
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
				//System.out.println(palavraAtual[i]);

			}

			try {

				if (tamanhoDoGrupoDeBits > 8) {
					if (totalBytes > 0) {
						totalBytes--;
						escreveDoisBytes(caracteres[0], fos);
					} else {
						int escreveAux = caracteres[0] & 0xFF; // descarta o byte mais significativo
						fos.write(escreveAux);
					}
				} else {
					byteAux = juntaArray(caracteres);
					fos.write(byteAux);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Problema na escrita.");
				System.exit(0);
			}
			//System.exit(0);
		}

		tempoDepois = System.currentTimeMillis();
		System.out.println("Decodificação concluída em: " + (tempoDepois - tempoAntes) / 1000.0 + "s");
	}

	public static void copiaConteudo (FileOutputStream escreve, FileInputStream le) {
		byte dataBlock[] = new byte[1024];
		int lidos;
		try {
			while ((lidos = le.read(dataBlock)) != -1) {
				for (int i = 0; i < lidos; i++)
					escreve.write(dataBlock[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problema na leitura.");
			System.exit(0);
		}
	}

	public static char decodificaContextoMenosUm (int i) {
		Vector <Character> decodificados = valoresDecodificados.get(i);
		int retorno = decodificador[i].getCurrentSymbolCount(totalContextoMenosUm[i]);
		//System.out.println(retorno);
		char ch = encontraSimboloMenosUm(retorno, decodificados);
		//decodificados.add(ch);
		insereOrdenado(decodificados, ch);
		
		caracteres[i] = ch;
		//System.out.println("Ch dentro: " + (int) ch);
		int lht [] = new int[3];
		lht[0] = retorno;
		lht[1] = retorno + 1;
		lht[2] = totalContextoMenosUm[i]--;
		//System.out.println(lht[0]);
		try {
			//System.out.println("Decodificando 3: ");
			//TestTrie.mostra(lht);
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

		//System.out.print("Simbolo: " + simbolo + "|");

		//Collections.sort(vetorDecodificados);

		for (int i = 0; i < vetorDecodificados.size(); i++) {
			//System.out.print(" " + (int)vetorDecodificados.get(i).charValue());
			if (vetorDecodificados.get(i).charValue() <= aux) {
				aux++;
				continue;
			} else
				break;
		}
		//System.out.println(" valor final: " + (int)aux);
		//if (vetorDecodificados.indexOf(aux) != -1) aux++;
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
	
	public static void insereOrdenado (Vector<Character> vetor, char ch) {
		for (int i = 0; i < vetor.size(); i++) {
			int aux = (int) vetor.get(i).charValue();
			if (ch < aux) {
				vetor.add(i, ch);
				return;
			}
		}
		vetor.add(ch);
	}
}
