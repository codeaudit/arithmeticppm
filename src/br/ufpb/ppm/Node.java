package br.ufpb.ppm;

public class Node {

	char conteudo;
	boolean marcador; // marcador de fim de palavra 
	Node[] filhos;
	int contador; // para o PPM

	public Node()
	{
		this((byte) 8);
	}
	
	public Node (byte tamanhoDoBlocoDeBits) {
		marcador = false;
		filhos = new Node[(int) Math.pow(2, tamanhoDoBlocoDeBits)];
		contador = 1;
	}

	public Node(byte tamanhoDoBlocoDeBits, int caracter)
	{
		conteudo = (char)caracter;
		marcador = false;
		filhos = new Node[(int) Math.pow(2, tamanhoDoBlocoDeBits)];
		contador = 1;
	}

}
