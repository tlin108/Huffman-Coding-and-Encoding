import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;


public class Main {
	
	public static void CreateHistogramTable(Scanner inFile, PrintWriter pWriter){
		int[] hist = new int[256]; double count=0;
		
		FillingHistogramTable(hist, inFile, pWriter, count);
	}
	
	public static void FillingHistogramTable(int[] hist, Scanner inFile, PrintWriter pWriter, double count){
		String DataIn; int index; char CharIn;
		while(inFile.hasNext()){
			DataIn = inFile.next();
			CharIn = DataIn.charAt(0);
			index = (int)CharIn;
			hist[index]++;
			count++;
		}
		PrintHistToOutput(hist, pWriter, count);
	}
	
	public static void PrintHistToOutput(int [] hist, PrintWriter pWriter, double count){
		char Char; double prob=0;
		for(int i=0;i<256;i++){
			if(hist[i]!=0){
				if(i==10){
					prob=((hist[i]/count)*100);
					pWriter.println ("~"+" "+(int)prob);
				}else if(i==13){
					prob=((hist[i]/count)*100);
					pWriter.println ("#"+" "+(int)prob);
				}else if(i==32){
					prob=((hist[i]/count)*100);
					pWriter.println ("_"+" "+(int)prob);
				}
				else{
					Char = (char)i;
					prob=((hist[i]/count)*100);
					pWriter.println (Char+" "+(int)Math.ceil(prob));
				}
			}
		}
	}
	 
	public static void main(String[] args){
		String charIn,code="";int probIn,N=0;Node newnode;Node root;
		List link=new List();
		
		Scanner inFile;
		try{
			Scanner scanner = new Scanner(new FileReader(args[0]));
			inFile = scanner.useDelimiter("");
			File outFile = new File(args[1]);
			FileWriter fWriter = new FileWriter (outFile);
			PrintWriter pWriter = new PrintWriter (fWriter);
			
			//step (a)
			CreateHistogramTable(inFile,pWriter);
			fWriter.flush();
			pWriter.flush();
			
			inFile = new Scanner(new FileReader(args[1]));
			outFile = new File(args[2]);
			fWriter = new FileWriter (outFile);
			pWriter = new PrintWriter (fWriter);
	
			while(inFile.hasNext()){
				N++;
				charIn = inFile.next();
				probIn = inFile.nextInt();
				newnode = new Node(charIn,probIn);
				link.HuffmanLinkedList(newnode);
			}			
			
			link.print_list(pWriter);
			pWriter.println("");
			pWriter.println("Huffman Data In A Binary Tree");			
			link.HuffmanBinaryTree(pWriter);
			pWriter.println("");
			pWriter.println("Entropy Tablez");
			pWriter.println ("Char"+"\t"+"Prob"+"\t"+"Code"+"     \t\t"+"Bits"+"\t"+"Entropy");
			root=link.getHead().next;
			link.HuffmanCode(root, code);
			link.EntropyTable(pWriter,root,N);
			
			fWriter.flush();
			pWriter.flush();
			
			//step (b)
			scanner = new Scanner(new FileReader(args[3]));
			inFile = scanner.useDelimiter("");
			outFile = new File(args[4]);
			fWriter = new FileWriter (outFile);
			pWriter = new PrintWriter (fWriter);
			
			link.encode(inFile, pWriter, N);
			

			fWriter.flush();
			pWriter.flush();
			
			//step(c)
			scanner = new Scanner(new FileReader(args[4]));
			inFile = scanner.useDelimiter("");
			outFile = new File(args[5]);
			fWriter = new FileWriter (outFile);
			pWriter = new PrintWriter (fWriter);
			
			link.decode(inFile, pWriter, N);
			
			inFile.close();
			scanner.close();
			fWriter.close();
			pWriter.close();
			
			
		} catch(FileNotFoundException e){
			System.out.println("File not found");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
