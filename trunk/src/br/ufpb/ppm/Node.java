package br.ufpb.ppm;

public class Node {

	char content;
	boolean marker; /* The marker denotes the end of an word.
					 * This is to ensure that a substring is not 
					 * found if it was not explicitly inserted.
					 * For example, if "manly" is inserted, and
					 * "man" is not, then search for the word
					 * "man" should be unsuccessful. 
					 */
	Node[] child;
	int contador; // para o PPM

	

	public Node()
	{
		marker=false;
		// One child corresponding to one lowercase alphabet
		child = new Node[26];
		contador = 1;
	}

	

	public Node(int i)
	{
		// Cell 0 corresponds to the character 'a'
		// Cell 1 corresponds to the character 'b'
		// ... and so on
		content = (char)((int)'a'+i);
		marker=false;
		child = new Node[26];
		contador = 1;
	}

}
