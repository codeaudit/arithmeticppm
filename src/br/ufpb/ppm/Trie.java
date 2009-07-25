package br.ufpb.ppm;

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
		return getLowHighTotal (s, false, new PseudoNo());
	}
	
	public int[] getLowHighTotal (String s, boolean atualiza, PseudoNo pseudoPai) {
		Node current = raiz;
		//pseudoPai.no = raiz;
		Node pai = raiz;
		Node aux;
		int indiceAux;
		int[] retorno = new int[3];

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if ((indiceAux = current.filhos.indexOf(new Node (s.charAt(i)))) == -1) {
					
					// retorna o escape
					retorno[0] = current.totalDeFilhos;
					retorno[1] = current.totalDeFilhos + current.totalEscape;
					retorno[2] = current.totalDeFilhos + current.totalEscape;
					pseudoPai.no = current;
					return retorno; // string nao encontrada
				} else {
					pai = current;
					current = current.filhos.get(indiceAux);
					// System.out.println("Caracter \"" + current.content
					// + "\" encontrado");
				}
			}
			retorno[0] = 0;
			
			for (int i = 0; i < pai.filhos.size(); i++) {
				if ((aux = pai.filhos.get(i)) == current)
					break;
				retorno[0] += aux.contador; 
			}
			retorno[1] = retorno[0] + current.contador;
			retorno[2] = pai.totalDeFilhos + pai.totalEscape;
			
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
}
