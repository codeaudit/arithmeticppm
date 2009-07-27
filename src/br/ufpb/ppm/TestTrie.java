package br.ufpb.ppm;

import com.colloquial.arithcode.ArithEncoder;

public class TestTrie {

	public static void main(String[] args) {

		Trie trie = new Trie(8);

		/*T.insert("google");
		T.insert("goblet");
		T.insert("yahoo");
		T.insert("");
		// T.insert("go");

		T.search("google");
		T.search("goblets");
		T.search("go");
		T.search("blah");
		T.search("");*/

		// testando para a string "zxzyzxxyzx”, exemplo do Complete Reference, pg. 145, com contexto 2
		String palavra = "zxzyzxxyzx"; // isso aqui é como se fosse o arquivo a ser codificado,
		// vamos lendo de um em um caractere

		palavra = "abracadabra"; // exemplo da sala
		String palavraAtual = "";
		//int teste2[] = trie.getLowHighTotal("a", true, new PseudoNo());
		//mostra (teste2);
		//teste2 = trie.getLowHighTotal("a", true, new PseudoNo());
		//mostra(teste2);
		int contexto = 3;

		for (int i = 0; i < palavra.length(); i++) {
			if (palavraAtual.length() <= contexto) {
				palavraAtual += palavra.charAt(i);
			} else {
				palavraAtual = palavraAtual.substring(1) + palavra.charAt(i);
			}

			if (!trie.procura(palavraAtual))
				trie.insere(palavraAtual);

			for(int j = 0; j < palavraAtual.length()-1; j++) {
				if (!trie.procura(palavraAtual.substring(j+1))) // se for encontrado, já aumenta o contador
					trie.insere(palavraAtual.substring(j+1)); // insere com contador 1 no contexto atual
			}
		}

		trie.percorre();

		//trie.percorrePorNivel();

		System.out.println(trie.getTotalMesmoContexto("ab", false));		
		System.out.println(trie.getTotalMesmoContexto("ab", true));
		System.out.println(trie.getTotalMesmoContexto("a", false));
		System.out.println(trie.getTotalMesmoContexto("a", true));
		System.out.println(trie.getTotalMesmoContexto("bra", false));
		System.out.println(trie.getTotalMesmoContexto("bra", true));
		System.out.println(trie.getTotalMesmoContexto("abr", false));
		System.out.println(trie.getTotalMesmoContexto("abr", true));
		int[] teste = trie.getLowHighTotal("a");
		mostra (teste);
		teste = trie.getLowHighTotal("b");
		mostra (teste);
		teste = trie.getLowHighTotal("r");
		mostra (teste);
		teste = trie.getLowHighTotal("c");
		mostra (teste);
		teste = trie.getLowHighTotal("d");
		mostra (teste);
		teste = trie.getLowHighTotal("l"); // escape
		mostra (teste);
		teste = trie.getLowHighTotal("ab");
		mostra (teste);
		teste = trie.getLowHighTotal("az"); // escape
		mostra (teste);
		
		comprime("ab", trie);

		//System.out.println(trie.getTotalMesmoContexto("yzx"));
		//System.out.println(trie.getTotalMesmoContexto("x"));
		//System.out.println(trie.getTotalMesmoContexto("zx"));
		//System.out.println(trie.getTotalMesmoContexto("zy"));

	}

	public static void mostra (int [] array) {
		System.out.print("Valores: ");
		for (int i = 0; i < array.length; i++)
			System.out.print(array[i] + " ");
		System.out.println();
	}

	public static void comprime(String s, Trie arvore) {
		PseudoNo paiAux = new PseudoNo();
		//Node paiAux = null;
		int[] lht;

		//lht = arvore.getLowHighTotal(s, true, paiAux);
		System.out.println(paiAux.no);
		paiAux.no.totalDeFilhos = 15;
		//mostra(lht);
	}

}