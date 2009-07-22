package br.ufpb.ppm;

public class Node {

	char content;
	boolean marker; /* marker denota o fim de uma palavra
					 * Isto é para garantir que uma substring não é
					 * encontrada se não foi inserida explicitamente.
					 * Por exemplo, se "manly" foi inserido, e
					 * "man" não foi, então a busca por
					 * "man" deve falhar. 
					 */
	Node[] child;
	int contador; // para o PPM

	

	public Node()
	{
		this((byte) 8);
	}
	
	public Node (byte quantidade) {
		marker=false;
		child = new Node[(int) Math.pow(2, quantidade)];
		contador = 1;
	}

	

	public Node(byte quantidade, int character)
	{
		content = (char)character;
		marker=false;
		child = new Node[(int) Math.pow(2, quantidade)];
		contador = 1;
	}

}
