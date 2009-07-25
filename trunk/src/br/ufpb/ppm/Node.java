package br.ufpb.ppm;

import java.util.Vector;

public class Node {

	char conteudo;
	boolean marcador; // marcador de fim de palavra 
	//Node[] filhos;
	Vector<Node> filhos;
	int contador; // para o PPM
	int totalDeFilhos;
	int totalEscape;

	public Node(int caracter)
	{
		conteudo = (char)caracter;
		marcador = false;
		//filhos = new Node[(int) Math.pow(2, tamanhoDoBlocoDeBits)];
		filhos = new Vector<Node> ();
		contador = 1;
		totalDeFilhos = 0;
		totalEscape = 0;
	}
	
	public Node(int caracter, int capacidade)
	{
		conteudo = (char)caracter;
		marcador = false;
		//filhos = new Node[(int) Math.pow(2, tamanhoDoBlocoDeBits)];
		filhos = new Vector<Node> (capacidade, capacidade);
		contador = 1;
		totalDeFilhos = 0;
		totalEscape = 0;
	}
	
	public boolean equals (Object o) {
		Node aux = (Node) o;
		return conteudo == aux.conteudo;
	}
	
	public void incrementaEscape (int maxCaracteres) { // valor maximo do escape
		totalEscape++;
		if (totalEscape == maxCaracteres) totalEscape = 0;
	}

}
