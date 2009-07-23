package br.ufpb.ppm;

public class Trie {

	private Node raiz;
	private byte tamanhoDoBlocoDeBits;

	public Trie() {
		this((byte) 8);
	}

	public Trie(byte tamanhoDoBlocoDeBits) {
		this.tamanhoDoBlocoDeBits = tamanhoDoBlocoDeBits;
		raiz = new Node(tamanhoDoBlocoDeBits);
		raiz.conteudo = 0;
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
		
		if (inserindo.length() == 0)
			current.marcador = true;
		for (int i = 0; i < inserindo.length(); i++) {
			if (current.filhos[(int)inserindo.charAt(i)] != null) {
				current = current.filhos[(int)inserindo.charAt(i)];
			} else {
				current.filhos[(int)inserindo.charAt(i)] = 
					new Node(tamanhoDoBlocoDeBits, (int)inserindo.charAt(i));
				current = current.filhos[(int)inserindo.charAt(i)];
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
		while (noAtual != null) {
			for (int i = 0; i < procurada.length(); i++) {
				if (noAtual.filhos[(int) procurada.charAt(i)] == null) {
					return false;
				} else {
					noAtual = noAtual.filhos[(int) procurada.charAt(i)];
				}
			}
			// Garantir que substrings indesejadas não serão encontradas:
			if (noAtual.marcador == true) {
				noAtual.contador++;
				return true;
			} else {
				noAtual.contador++;
				return false;
			}
		}
		return false;
	}

	// pega a quantidade de filhos no contexto da String passada, sera utilizado
	// no aritmetico
	public int getTotalMesmoContexto(String s) {
		Node current = raiz;
		Node pai = raiz;
		int total = 0;

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if (current.filhos[(int) s.charAt(i)] == null) {
					return 0; // string nao encontrada
				} else {
					pai = current;
					current = current.filhos[(int) s.charAt(i)];
					// System.out.println("Caracter \"" + current.content
					// + "\" encontrado");
				}
			}
			for (int i = 0; i < pai.filhos.length; i++) {
				if (pai.filhos[i] != null) {
					total += pai.filhos[i].contador;
				}
			}
			return total;
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
		System.out.print("Caractere: " + no.conteudo + "\nContagem: "+ no.contador);
		System.out.println("\tNivel: " + nivel + "\n");
		for (int i = 0; i < no.filhos.length; i++) {
			if (no.filhos[i] != null) {
				System.out.print("Vendo filhos de: ");
				if (nivel == 0) {
					System.out.println("raiz");
				} else {
					System.out.println("[caractere/nivel]" + no.conteudo + "/"+ no.contador);
				}
				percorre(no.filhos[i], nivel + 1);
			}
		}
	}

}
