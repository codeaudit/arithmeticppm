package br.ufpb.ppm;

import java.util.Vector;

import sun.misc.Queue;

public class Trie {

	private Node raiz;
	private int maxCaracteres;
	private int inicializaVetor;

	public Trie() {
		this(0);
	}
	
	public Trie (int tamanhoDoGrupoDeBits) {
		this.maxCaracteres = (int) Math.pow (2, tamanhoDoGrupoDeBits);
		switch (tamanhoDoGrupoDeBits) {
		case 1:
		case 2:
		case 4:
			inicializaVetor = maxCaracteres;
			raiz = new Node (0, inicializaVetor);
			break;
		case 8:
			inicializaVetor = 128;
			raiz = new Node (0, inicializaVetor);
			break;
		case 16:
			inicializaVetor = 512;
			raiz = new Node (0, inicializaVetor);
			break;
		default:
			inicializaVetor = 0;
			raiz = new Node (0);
		}
	}

	/**
	 * 
	 * Método que insere uma String na árvore dos contextos
	 * @param inserindo Um array de chars, um char possui 2 bytes e é unsigned, 
	 * ou seja, pode armazenar valores de 0 a 65535. 
	 * Com isso, se pode armazenar valores desde grupos de 1 bit a grupos 
	 * de 16 bits.
	 * 
	 */
	public void insere(String inserindo) {
		Node current = raiz;
		int indiceAux;
		
		if (inserindo.length() == 0)
			current.marcador = true;
		for (int i = 0; i < inserindo.length(); i++) {
			if ((indiceAux = current.filhos.indexOf(new Node (inserindo.charAt(i)))) != -1) {
				current = current.filhos.get(indiceAux);
			} else {
				current.filhos.add(new Node(inserindo.charAt(i), inicializaVetor));
				current.totalDeFilhos++;
				current.incrementaEscape(maxCaracteres);
				current = current.filhos.get(current.filhos.size() - 1);
			}
			if (i == inserindo.length() - 1)
				current.marcador = true;
		}
	}

	/**
	 * 
	 * Procura por uma determinada String na árvore
	 * 
	 * @param procurada
	 *            String a ser procurada na árvore
	 * @return <i>True</i> caso seja encontrada e <i>False</i> caso contrário
	 * 
	 */
	public boolean procura(String procurada) {
		Node noAtual = raiz;
		Node pai = raiz;
		int indiceAux;
		
		while (noAtual != null) {
			for (int i = 0; i < procurada.length(); i++) {
				if ((indiceAux = noAtual.filhos.indexOf(new Node (procurada.charAt(i)))) == -1) {
					return false;
				} else {
					pai = noAtual;
					noAtual = noAtual.filhos.get(indiceAux);
				}
			}
			
			noAtual.contador++;
			pai.totalDeFilhos++;
			
			// Garantir que substrings indesejadas não serão encontradas:
			if (noAtual.marcador == true)
				return true;
			else
				return false;
		}
		return false;
	}
	
	public int getTotalMesmoContexto (String s) {
		return getTotalMesmoContexto(s, false);
	}

	// pega a quantidade de filhos no contexto da String passada, sera utilizado
	// no aritmetico
	// IMPORTANTE: o contexto utilizado é o da palavra atual, e não a palavra atual
	// Exemplo: "abc" retornaria o total de filhos no contexto "ab"
	
	public int getTotalMesmoContexto(String s, boolean escape) {
		Node current = raiz;
		Node pai = raiz;
		int indiceAux;

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if ((indiceAux = current.filhos.indexOf(new Node (s.charAt(i)))) == -1) {
					return 0; // string nao encontrada
				} else {
					pai = current;
					current = current.filhos.get(indiceAux);
					// System.out.println("Caracter \"" + current.content
					// + "\" encontrado");
				}
			}
			return escape ? pai.totalDeFilhos + pai.totalEscape : pai.totalDeFilhos;
		}
		return 0;
	}
	
	public int[] getLowHighTotal (String s) {
		return getLowHighTotal (s, false, new PseudoNo(), null);
	}
	
	public int[] getLowHighTotal (String s, boolean atualiza, PseudoNo pseudoPai, StringBuffer passagem) {
		StringBuffer exclusao = new StringBuffer ();
		Node current = raiz;
		//pseudoPai.no = raiz;
		Node pai = raiz;
		Node aux;
		int indiceAux;
		int[] retorno = new int[3];

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if (s.length() > 1 && i == s.length() - 1) { // logo antes do simbolo a ser codificado
					//System.out.println("Dentro. String: " +s + " Exclusao: " + exclusao);
					if ((indiceAux = procuraVetor(current.filhos, s.charAt(i), exclusao)) == -1) { // retorna o escape
						//System.out.println("Dentro. String: " +s + " Exclusao: " + exclusao);
						int totalDeFilhosExclusao = getTotalDeFilhosExclusao(current, passagem);
						retorno[0] = totalDeFilhosExclusao;
						retorno[1] = totalDeFilhosExclusao + current.totalEscape;
						retorno[2] = totalDeFilhosExclusao + current.totalEscape;
						pseudoPai.no = current;
						juntaStrings(passagem, exclusao);
						//System.out.print("Escape 1: ");
						//TestTrie.mostra(retorno);
						//System.out.println(totalDeFilhosExclusao);
						return retorno; // string nao encontrada				
					} else {
						pai = current;
						current = current.filhos.get(indiceAux);
					}
				} else {
					if ((indiceAux = current.filhos.indexOf(new Node (s.charAt(i)))) == -1) {

						// retorna o escape
						//juntaStrings(passagem, exclusao);
						int totalDeFilhosExclusao = getTotalDeFilhosExclusao(current, passagem);
						retorno[0] = totalDeFilhosExclusao;
						retorno[1] = totalDeFilhosExclusao + current.totalEscape;
						retorno[2] = totalDeFilhosExclusao + current.totalEscape;
						pseudoPai.no = current;
						//System.out.print("Escape 2: ");
						//TestTrie.mostra(retorno);
						return retorno; // string nao encontrada
					} else {
						pai = current;
						current = current.filhos.get(indiceAux);
						// System.out.println("Caracter \"" + current.content
						// + "\" encontrado");
					}
				}
			}
			retorno[0] = 0;
			String stringAux;
			int totalDeFilhosExclusao = pai.totalDeFilhos;
			boolean encontrado = false;
			//System.out.println("Qtd filhos: " + pai.filhos.size());
			for (int i = 0; i < pai.filhos.size(); i++) {
				//System.out.print("Mostrando filho: ");
				//System.out.println((int) pai.filhos.get(i).conteudo);
				//System.out.println("Tamanho da exclusao: " +passagem.length());
				if ((aux = pai.filhos.get(i)) == current)
					encontrado = true;
				
				stringAux = "";
				stringAux += aux.conteudo;
				if (passagem.indexOf(stringAux) == -1) {
					if (!encontrado)
						retorno[0] += aux.contador; 
				} else
					totalDeFilhosExclusao--;
			}
			retorno[1] = retorno[0] + current.contador;
			retorno[2] = totalDeFilhosExclusao + pai.totalEscape;
			//System.out.println("Utilizado/normal: " +retorno[2]+"/"+(pai.totalDeFilhos + pai.totalEscape));
			
			
			//TestTrie.mostra(retorno);
			//System.out.println(pai);
			
			pseudoPai.no = pai;
			
			if (atualiza) {
				current.contador++;
				pai.totalDeFilhos++;
			}
			
			return retorno;
		}
		return null;
	}
	
	public int getTotalDeFilhosExclusao (Node no, StringBuffer exclusao) {
		int total = no.totalDeFilhos;
		Node aux;
		String aux2;
		for (int i = 0; i < no.filhos.size(); i++) {
			aux2 = "";
			aux = no.filhos.get(i);
			aux2 += aux.conteudo;
			if (exclusao.indexOf(aux2) != -1)
				total--;
		}
		return total;
	}
	
	public int getTotalDeFilhosExclusaoEAtualiza (Node no, StringBuffer exclusao, StringBuffer exclusaoAux) {
		int total = no.totalDeFilhos;
		//System.out.println("Total: " + total);
		StringBuffer stringAux = new StringBuffer();
		Node aux;
		String aux2;
		for (int i = 0; i < no.filhos.size(); i++) {
			aux2 = "";
			aux = no.filhos.get(i);
			//if (aux.contador == 0) continue;
			aux2 += aux.conteudo;
			
			stringAux.append(aux.conteudo);
			if (exclusao.indexOf(aux2) != -1)
				total--;
		}
		juntaStrings(exclusaoAux, stringAux);
		return total;
	}
	
	// metodo para inserir um simbolo em um nó, por conta da maneira que é implementado
	// o programa (sendo esse método chamado apenas quando há garantias de que determinado
	// caractere não é filho do nó passado, não é feita nenhuma verificação de pertinência
	public void insereEmNo (Node no, char insere) {
		Node aux = new Node(insere, inicializaVetor);
		aux.marcador = true;
		no.filhos.add(aux);
		no.totalDeFilhos++;
		no.incrementaEscape(maxCaracteres);
	}
	
	public void buscaEInsereEmNo (Node no, char insere) {
		Node aux = new Node(insere, inicializaVetor);
		Node aux2;
		
		int indiceAux = no.filhos.indexOf(aux);
		if (indiceAux != -1) {
			aux2 = no.filhos.get(indiceAux);
			aux2.contador++;
			no.totalDeFilhos++;
			return;
		}
		
		// se nao for encontrado, insere
		aux.marcador = true;
		no.filhos.add(aux);
		no.totalDeFilhos++;
		no.incrementaEscape(maxCaracteres);
	}
	
	public void juntaStrings (StringBuffer stringUm, StringBuffer stringDois) {
		String aux;
		for (int i = 0; i < stringDois.length(); i++) {
			aux = "";
			aux += stringDois.charAt(i);
			if (stringUm.indexOf (aux) == -1)
				stringUm.append(aux);
		}
	}
	
	public int procuraVetor(Vector<Node> vetor, char ch, StringBuffer encontrados) {
		Node aux;
		for (int i = 0; i < vetor.size(); i++) {
			aux = vetor.get(i);
			if (aux.equals(new Node (ch)))
				return i;
			encontrados.append(aux.conteudo);
		}
		return -1;
	}

	public void percorre() {
		percorre(raiz, 0);
	}

	/**
	 * 
	 * Método recursivo que percorre a árvore passada como argumento no sentido
	 * in-ordem.
	 * 
	 * @param no
	 * @param nivel
	 * 
	 */
	public void percorre(Node no, int nivel) {
		if (no == null)
			return;
		
		if (nivel != 0)
			System.out.print("Caractere: " + no.conteudo + "\nContagem: "+ no.contador);
		else
			System.out.print("Caractere: raiz" + "\nContagem: "+ no.contador);
		System.out.println("\tNivel: " + nivel + "\n");
		for (int i = 0; i < no.filhos.size(); i++) {
			System.out.print("Vendo filhos de: ");
			if (nivel == 0) {
				System.out.println("raiz");
			} else {
				System.out.println("[caractere/nivel/contagem]"+no.conteudo+"/"+nivel+"/"+no.contador);
			}
			percorre(no.filhos.get(i), nivel + 1);
		}
	}
	
	/**
	 * 
	 * Método para percorrer a árvore por nível
	 * 
	 */
	
	public void percorrePorNivel () {
		Queue queue = new Queue ();
		
		Node aux;
	
		if (raiz != null) {
			queue.enqueue(raiz);
			
			while (!queue.isEmpty()) {
				try {
					aux = (Node) queue.dequeue();
					
					if (maxCaracteres < 65536)
						System.out.println("Caractere: " + aux.conteudo);
					else
						System.out.println("Caractere: " + (int) aux.conteudo);
					
					System.out.println("Contagem: " + aux.contador + "\n");
					
					if (aux != null) {
						for (int i = 0; i < aux.filhos.size(); i++) {
							queue.enqueue(aux.filhos.get(i));
						}
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public Vector<PseudoNo> retornaContextos (String s, Vector<PseudoNo> vetor) {
		Vector<PseudoNo> retorno;
		if (vetor == null)
			retorno = new Vector<PseudoNo> ();
		else
			retorno = vetor;
		
		Node current = raiz;
		Node pai = raiz;
		int indiceAux;

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if ((indiceAux = current.filhos.indexOf(new Node (s.charAt(i)))) == -1) {
					return null; // string nao encontrada
				} else {
					pai = current;
					current = current.filhos.get(indiceAux);
					// System.out.println("Caracter \"" + current.content
					// + "\" encontrado");
				}
			}
			
			retorno.add(new PseudoNo(current));
			if (s.length() > 1)
				return retornaContextos (s.substring(1), retorno);
			else if (s.length() == 1) {
				retorno.add(new PseudoNo (pai));
				return retorno;
			} else {
				return retorno;
			}
			
		}
		return null;
	}
	
	public int [] retornaNoPeloLow (Node no, int retorno, char caracteres[], int j, StringBuffer exclusao) {
		int lht[] = new int [3];
		int total = 0;
		int totalDeFilhos = no.totalDeFilhos;
		String stringAux;
		int indiceAux;
		//System.out.println(retorno);
		boolean encontrado = false;
		
		//System.out.println("Qtd filhos: " + no.filhos.size());		
		for (int i = 0; i < no.filhos.size(); i++) {
			Node aux = no.filhos.get(i);
			//System.out.print("Mostrando filho: ");
			//System.out.println((int) aux.conteudo);
			stringAux = "";
			stringAux += aux.conteudo;
			indiceAux = exclusao.indexOf(stringAux);
			//System.out.println("Tamanho exclusao: " + exclusao.length() + "\\" + indiceAux);
			if (indiceAux != -1) {
				totalDeFilhos -= aux.contador;
				continue;
			}
			//System.out.println("Tamanho exclusao: " + exclusao.length() + "\\" + indiceAux);
			if (retorno >= total && retorno < total + aux.contador) {
				if (!encontrado) {
					encontrado = true;
					lht[1] = aux.contador;

					if (caracteres.length > j)
						caracteres[j] = aux.conteudo;
					break;
					//return lht;
				}
			}
			if (!encontrado)
				total += aux.contador;
			
		}
		
		lht[0] = total;
		lht[1] += total;
		lht[2] = 0;
		return lht;
	}
	
}
