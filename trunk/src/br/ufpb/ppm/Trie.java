package br.ufpb.ppm;

public class Trie {

	private Node raiz;
	//private byte tamanhoDoBlocoDeBits;

	public Trie() {
		this(0);
	}
	
	public Trie (int tamanhoDoGrupoDeBits) {
		switch (tamanhoDoGrupoDeBits) {
		case 1:
		case 2:
		case 4:
			raiz = new Node (0, (int) Math.pow(2, tamanhoDoGrupoDeBits));
			break;
		case 8:
			raiz = new Node (0, 128);
			break;
		case 16:
			raiz = new Node (0, 512);
			break;
		default:
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
				current.filhos.add(new Node(inserindo.charAt(i)));
				current.totalDeFilhos++;
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

	// pega a quantidade de filhos no contexto da String passada, sera utilizado
	// no aritmetico
	public int getTotalMesmoContexto(String s) {
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
			return pai.totalDeFilhos;
		}
		return 0;
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
}
