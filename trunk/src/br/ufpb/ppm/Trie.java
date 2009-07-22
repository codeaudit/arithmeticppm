package br.ufpb.ppm;

public class Trie {

	private Node root;
	private byte quantidade;

	public Trie() // Construtor
	{
		this((byte) 8);
	}
	
	public Trie (byte quantidade) {
		this.quantidade = quantidade;
		root = new Node(quantidade);
		root.content = (char) -1; // Raiz contém um caracter diferente dos
									// primeiros 256.
	}

	public void insert(String s) {
		Node current = root;

		if (s.length() == 0) // Para String vazia
			current.marker = true;

		for (int i = 0; i < s.length(); i++) {

			/*
			 * Primeira visita e visita repetida são diferenciados para evitar
			 * sobrescrever os valores de child[] durante a nova visita
			 */

			if (current.child[(int) s.charAt(i)] != null) // Visita repetida
			{
				current = current.child[(int) s.charAt(i)];
				//System.out.println("Caracter Inserido: " + current.content);
			}

			else // Primeira visita
			{
				current.child[(int) s.charAt(i)] = new Node(quantidade, (int) s.charAt(i));
				current = current.child[(int) (s.charAt(i))];
				//System.out.println("Caracter Inserido: " + current.content);
			}
			// Coloca o marcado para indicar fim da palavra
			if (i == s.length() - 1)
				current.marker = true;
		}
		//System.out.println("Finalizado inserindo palavra: " + s + "\n");
	}

	public boolean search(String s) {
		Node current = root;
		//System.out.println("\nProcurando por string: " + s);

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if (current.child[(int) s.charAt(i)] == null) {
					//System.out.println("String \"" + s + "\" não encontrada");
					return false;
				} else {
					current = current.child[(int) s.charAt(i)];
					//System.out.println("Caracter \"" + current.content
					//		+ "\" encontrado");
				}
			}
			// String existe
			// Mas para garantir que substrings indesejadas não serão
			// encontradas:

			if (current.marker == true) {
				//System.out.println("String encontrada: " + s);
				current.contador++;
				return true;
			} else {
				//System.out.println("String não encontrada: " + s
				//		+ "(presente apenas como substring)");
				current.contador++;
				return false;
			}
		}

		return false;
	}
	
	// pega a quantidade de filhos no contexto da String passada, sera utilizado no aritmetico
	public int getTotalMesmoContexto (String s) {
		Node current = root;
		Node pai = root;
		int total = 0;

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if (current.child[(int) s.charAt(i)] == null) {
					return 0; // string nao encontrada
				} else {
					pai = current;
					current = current.child[(int) s.charAt(i)];
					//System.out.println("Caracter \"" + current.content
					//		+ "\" encontrado");
				}
			}
			
			for (int i = 0; i < pai.child.length; i++) {
				if (pai.child[i] != null) {
					total += pai.child[i].contador;
				}
			}
			return total;
		}

		return 0;
	}

	public void percorre() {
		percorre(root, 0);
	}

	public void percorre(Node no, int nivel) {
		if (no == null)
			return;

		System.out.print("Caractere: " + no.content + "\nContagem: "
				+ no.contador);
		System.out.println("\tNivel: " + nivel + "\n");

		for (int i = 0; i < no.child.length; i++) {
			if (no.child[i] != null) {
				
				System.out.print("Vendo filhos de: ");
				
				if (nivel == 0)
					System.out.println("raiz");
				else
					System.out.println("[caractere/nivel]" + no.content + "/" + no.contador);
				
				percorre(no.child[i], nivel + 1);
			}
		}
	}

}
