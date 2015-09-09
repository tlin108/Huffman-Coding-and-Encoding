import java.io.PrintWriter;
import java.util.Scanner;


public class List {
	Node dummyHead;
	
	static String[] Char;
	static int[]	Prob;
	static String[] Code;
	static int[]	Bits;
	static int[] Entropy;
	
	List(){
		dummyHead = new Node();
	}
	
	Node getHead(){
		return dummyHead;
	}
	
	void HuffmanLinkedList(Node newnode){
		Node walker = dummyHead;
		while(walker.next!=null&&walker.next.Prob<newnode.Prob){
			walker=walker.next;
		}
		newnode.next=walker.next;
		walker.next=newnode;
	}
	
	void HuffmanBinaryTree(PrintWriter pWriter){
		Node newListHead = dummyHead;
		print_tree(pWriter);
		while(newListHead.next.next!=null){
			Node newnode= new Node();
			newnode= new Node();
			newnode.CharStr=newListHead.next.CharStr+newListHead.next.next.CharStr;
			newnode.Prob=newListHead.next.Prob+newListHead.next.next.Prob;
			newnode.left=newListHead.next;
			newnode.right=newListHead.next.next;
			newListHead.next=newListHead.next.next.next;
			
			HuffmanLinkedList(newnode);
			print_tree(pWriter);
		}
	}
	
	void print_list(PrintWriter pWriter){
		Node print=dummyHead.next;
		pWriter.println ("Sorted Huffman Data");
		pWriter.println ("");
		while(print!=null){
			pWriter.println (print.CharStr+" "+print.Prob);
			print=print.next;
		}
	}
	
	void print_tree(PrintWriter pWriter){
		Node print=dummyHead;
		pWriter.print("listHead ->(dummy, 0, "+print.next.CharStr+")-> ");
		print=print.next;
		while(print.next!=null){
			pWriter.print("("+print.CharStr+", "+print.Prob+", "+print.next.CharStr+")->");
			print=print.next;
		}
		pWriter.print("("+print.CharStr+", "+print.Prob+", null)-> Null");
		pWriter.println("\n");
		pWriter.flush();
	}

	void HuffmanCode(Node T, String code){
		Node walker=T;
		if(walker==null)
			return;
		
		if(walker.left==null&&walker.right==null){
			walker.Code=code;
			return;
		}
		
		HuffmanCode(walker.left,code+"0");
		HuffmanCode(walker.right,code+"1");
	}
	
	void EntropyTable(PrintWriter pWriter, Node T, int N){
		Char=new String[N];
		Prob=new int[N];
		Code=new String[N];
		Bits=new int[N];
		Entropy=new int[N];
		
		FillingTable(T,Char,Prob,Code,Bits,Entropy);
		printEntropyTable(pWriter,Char,Prob,Code,Bits,Entropy,N);
	}
	
	void FillingTable(Node T,String[] Char,int[] Prob,String[] Code,int[] Bits, int[] Entropy){
		Node walker=T;int count=0;
		if(walker==null)
			return;
		
		if(walker.left==null&&walker.right==null){
			while(Prob[count]!=0){
				count++;
			}
			Char[count]=walker.CharStr;
			Prob[count]=walker.Prob;
			Code[count]=walker.Code;
			Bits[count]=walker.Code.length();
			Entropy[count]=walker.Prob*walker.Code.length();
			return;
		}
		FillingTable(walker.left,Char,Prob,Code,Bits,Entropy);
		FillingTable(walker.right,Char,Prob,Code,Bits,Entropy);
		
	}
	
	void printEntropyTable(PrintWriter pWriter, String[] Char,int[] Prob,String[] Code,int[] Bits, int[] Entropy, int N){
		int sum=0;
		for(int i=0;i<N;i++){
			pWriter.println(Char[i]+"\t"+Prob[i]+"\t"+Code[i]+"     \t\t"+Bits[i]+"\t"+Entropy[i]);
			sum+=Entropy[i];
		}
		sum=sum/100;
		pWriter.println("Total Entropy: "+sum);
	}
	
	void encode(Scanner inFile, PrintWriter pWriter, int N){
		String code, DataIn; char CharIn; int index;
		while(inFile.hasNext()){
			DataIn = inFile.next();
			CharIn = DataIn.charAt(0);
			index = (int)CharIn;
			if(index==32){
				for(int i=0;i<N;i++){
					if(Char[i].compareTo("_")==0){
						code=Code[i];
						pWriter.print(code);
					}
				}
			}else if(index==10){
				for(int i=0;i<N;i++){	
					if(Char[i].compareTo("~")==0){
						code=Code[i];
						pWriter.print(code);
					}
				}
			}else if(index==13){
				for(int i=0;i<N;i++){
					if(Char[i].compareTo("#")==0){
						code=Code[i];
						pWriter.print(code);
					}
				}
			}else{
				for(int i=0;i<N;i++){
					if(Char[i].compareTo(DataIn)==0){
						code=Code[i];
						pWriter.print(code);
					}
				}
			}
			pWriter.flush();
		}
	}
	
	void decode(Scanner inFile, PrintWriter pWriter, int N){
		int DataIn; String Codex="";
		while(inFile.hasNext()){
			DataIn = inFile.nextInt();
			Codex = Codex+DataIn;
			for(int i=0;i<N;i++){
				 if(Code[i].compareTo(Codex)==0&&Char[i].compareTo("_")==0){
					pWriter.print(" ");
					Codex="";
				}if(Code[i].compareTo(Codex)==0&&Char[i].compareTo("~")==0){
					pWriter.println("");
					Codex="";
				}if(Code[i].compareTo(Codex)==0&&Char[i].compareTo("#")==0){
					Codex="";	
				}else if(Code[i].compareTo(Codex)==0){
					pWriter.print(Char[i]);
					Codex="";
				}
			}
		}	
	}
}
