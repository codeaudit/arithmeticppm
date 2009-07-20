package br.ufpb.ppm;

public class TestTrie {

	public static void main(String[] args) {

		Trie T = new Trie();

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
		
		// testando para a string "zxzyzxxyzx”, exemplo do Complete Reference, pg. 145 com contexto 2
		String palavra = "zxzyzxxyzx";
		String palavraAtual = "";
		int contexto = 2;
		
		for (int i = 0; i < palavra.length(); i++) {
			if (palavraAtual.length() <= contexto) {
				palavraAtual += palavra.charAt(i);
			} else {
				palavraAtual = palavraAtual.substring(1) + palavra.charAt(i);
			}
			
			if (!T.search(palavraAtual))
				T.insert(palavraAtual);
			
			for(int j = 0; j < palavraAtual.length()-1; j++) {
				if (!T.search(palavraAtual.substring(j+1)))
					T.insert(palavraAtual.substring(j+1));
			}
		}
				
		T.percorre();

	}

}