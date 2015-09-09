

public class Node {
	Node next;
	Node right;
	Node left;
	String CharStr;
	String Code;
	int Prob;
	
	
	public Node(String Char, int prob){
		this.CharStr = Char;
		this.next = null;
		this.Prob = prob;
		this.right = null;
		this.left = null;
	}
	
	public Node(){
		this.CharStr = null;
		this.next = null;
		this.left = null;
		this.right = null;
		this.Prob = 0;
	}
}