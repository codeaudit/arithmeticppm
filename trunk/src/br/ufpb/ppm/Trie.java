package br.ufpb.ppm;

public class Trie {

	private Node root;

	public Trie() // Construtor
	{
		root = new Node();
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
				System.out.println("Caracter Inserido: " + current.content);
			}

			else // Primeira visita
			{
				current.child[(int) s.charAt(i)] = new Node((int) s.charAt(i));
				current = current.child[(int) (s.charAt(i))];
				System.out.println("Caracter Inserido: " + current.content);
			}
			// Coloca o marcado para indicar fim da palavra
			if (i == s.length() - 1)
				current.marker = true;
		}
		System.out.println("Finalizado inserindo palavra: " + s + "\n");
	}

	public boolean search(String s) {
		Node current = root;
		System.out.println("\nProcurando por string: " + s);

		while (current != null) {
			for (int i = 0; i < s.length(); i++) {
				if (current.child[(int) s.charAt(i)] == null) {
					System.out.println("String \"" + s + "\" não encontrada");
					return false;
				} else {
					current = current.child[(int) s.charAt(i)];
					System.out.println("Caracter \"" + current.content
							+ "\" encontrado");
				}
			}
			// String existe
			// Mas para garantir que substrings indesejadas não serão
			// encontradas:

			if (current.marker == true) {
				System.out.println("String encontrada: " + s);
				current.contador++;
				return true;
			} else {
				System.out.println("String não encontrada: " + s
						+ "(presente apenas como substring)");
				current.contador++;
				return false;
			}
		}

		return false;
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
				System.out.println("Vendo filhos de: [caractere/nivel]"
						+ no.content + "/" + no.contador);
				percorre(no.child[i], nivel + 1);
			}
		}
	}

}
