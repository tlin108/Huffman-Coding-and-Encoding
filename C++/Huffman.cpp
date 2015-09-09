#include<iostream>
#include<string>
#include<fstream>
#include<cmath>
using namespace std;

class Node{
public:
	
	Node* next;
	Node* right;
	Node* left;
	string CharStr;
	string Code;
	int Prob;

	Node(string Char, int prob){
		this->CharStr = Char;
		this->next = NULL;
		this->right = NULL;
		this->left = NULL;
		this->Prob = prob;
	}

	Node(){
		this->next = NULL;
		this->right = NULL;
		this->left = NULL;
		this->Prob = 0;
	}
};

class List{

public:
	Node* dummyHead;
	string* Char;
	int* Prob;
	string* Code;
	int* Bits;
	int* Entropy;
	
	List(){
		dummyHead = new Node();
	}
	
	Node* getHead(){
		return dummyHead;
	}
	
	void HuffmanLinkedList(Node* newnode){
		Node* walker = dummyHead;
		while(walker->next!=NULL&&walker->next->Prob<newnode->Prob){
			walker=walker->next;
		}
		newnode->next=walker->next;
		walker->next=newnode;
	}
	
	void HuffmanBinaryTree(ofstream &outFile){
		Node* newListHead = dummyHead;
		print_tree(outFile);
		while(newListHead->next->next!=NULL){
			Node* newnode= new Node();
			newnode->CharStr=newListHead->next->CharStr+newListHead->next->next->CharStr;
			newnode->Prob=newListHead->next->Prob+newListHead->next->next->Prob;
			newnode->left=newListHead->next;
			newnode->right=newListHead->next->next;
			newListHead->next=newListHead->next->next->next;
		
			HuffmanLinkedList(newnode);
			print_tree(outFile);
		}
	}
	
	void print_list(ofstream &outFile){
		outFile<<"Sorted Huffman Data"<<endl<<endl;
		Node*print=dummyHead->next;
		while(print!=NULL){
			outFile<<print->CharStr<<" "<<print->Prob<<endl;
			print=print->next;
		}
		outFile<<endl<<endl;
	}
	
	void print_tree(ofstream &outFile){
		Node* print=dummyHead;
		outFile<<"listHead -->(dummy, 0, "<<print->next->CharStr<<")--> ";
		print=print->next;
		while(print->next!=NULL){
			outFile<<"("<<print->CharStr<<", "<<print->Prob<<", "<<print->next->CharStr<<")-->";
			print=print->next;
		}
		outFile<<"("<<print->CharStr<<", "<<print->Prob<<", null)--> Null"<<endl<<endl;
	}
	
	void HuffmanCode(Node* T, string code){
		Node* walker=T;
		if(walker==NULL)
			return;
		
		if(walker->left==NULL&&walker->right==NULL){
			walker->Code=code;
			return;
		}
	
		HuffmanCode(walker->left,code+"0");
		HuffmanCode(walker->right,code+"1");
		
	}
	
	void EntropyTable(ofstream &outFile, Node* T, int N){
		Char=new string[N];
		Prob=new int[N];
		Code=new string[N];
		Bits=new int[N];
		Entropy=new int[N];
	
		FillingTable(T,Char,Prob,Code,Bits,Entropy);
		printEntropyTable(outFile,Char,Prob,Code,Bits,Entropy,N);
	}
	
	void FillingTable(Node* T,string Char[],int Prob[],string Code[],int Bits[],int Entropy[]){
		Node* walker=T;int count=0;
		if(walker==NULL)
			return;
			
		if(walker->left==NULL&&walker->right==NULL){
			while(Prob[count]){
				count++;
			}
			Char[count]=walker->CharStr;
			Prob[count]=walker->Prob;
			Code[count]=walker->Code;
			Bits[count]=walker->Code.length();
			Entropy[count]=walker->Prob*walker->Code.length();
			return;
		}
	
		FillingTable(walker->left,Char,Prob,Code,Bits,Entropy);
		FillingTable(walker->right,Char,Prob,Code,Bits,Entropy);
	}
	
	void printEntropyTable(ofstream &outFile,string Char[],int Prob[],string Code[],int Bits[],int Entropy[],int N){
		int sum=0;
		for(int i=0;i<N;i++){
			outFile<<Char[i]<<"\t"<<Prob[i]<<"\t"<<Code[i]<<"     \t\t"<<Bits[i]<<"\t"<<Entropy[i]<<endl;
			sum+=Entropy[i];
		}
		sum=sum/100;
		outFile<<"Total Entropy: "<<sum;
	}
	
	void encode(ifstream &inFile, ofstream &outFile, int N){
		string code; unsigned char CharIn; int index; string DataIn;
		inFile>>noskipws;
		
		while(!inFile.eof()){
			inFile>>CharIn;
			index = CharIn;
			if(index==32){
				for(int i=0;i<N;i++){
					if(Char[i].compare("_")==0){
						code=Code[i];
						outFile<<code;
					}
				}
			}else if(index==10){
				for(int i=0;i<N;i++){	
					if(Char[i].compare("~")==0){
						code=Code[i];
						outFile<<code<<endl;
					}
				}
			}else if(index==13){
				for(int i=0;i<N;i++){
					if(Char[i].compare("#")==0){
						code=Code[i];
						outFile<<code;
					}
				}
			}else{
				DataIn=string(1, CharIn);
				for(int i=0;i<N;i++){
					if(Char[i].compare(DataIn)==0){
						code=Code[i];
						outFile<<code;
					}
				}
			}
		}
	}
	
	void decode(ifstream &inFile, ofstream &outFile, int N){
		unsigned char DataIn; string Codex=""; string CharIn;
		while(!inFile.eof()){
			inFile>>DataIn;
			CharIn=string(1, DataIn);
			Codex = Codex+CharIn;
			for(int i=0;i<N;i++){
				 if(Code[i].compare(Codex)==0&&Char[i].compare("_")==0){
					outFile<<" ";
					Codex="";
				}if(Code[i].compare(Codex)==0&&Char[i].compare("~")==0){
					outFile<<endl;
					Codex="";
				}if(Code[i].compare(Codex)==0&&Char[i].compare("#")==0){
					Codex="";	
				}else if(Code[i].compare(Codex)==0){
					outFile<<Char[i];
					Codex="";
				}
			}
		}
	}
};
	void PrintHistToOutput(int hist[], ofstream &outFile, double count){
		char Char; double prob=0;
		for(int i=0;i<256;i++){
			if(hist[i]!=0){
				if(i==10){
					prob=((hist[i]/count)*100);
					outFile<<"~"<<" "<<ceil(prob)<<endl;
				}else if(i==13){
					prob=((hist[i]/count)*100);
					outFile<<"#"<<" "<<ceil(prob)<<endl;
				}else if(i==32){
					prob=((hist[i]/count)*100);
					outFile<<"_"<<" "<<ceil(prob)<<endl;
				}
				else{
					Char = i;
					prob=((hist[i]/count)*100);
					outFile<<Char<<" "<<ceil(prob)<<endl;
			}
			}
		} 
	}

	void FillingHistogramTable(ifstream &inFile, ofstream &outFile, int hist[], double count){
		string DataIn; int index; unsigned char CharIn;
		while(!inFile.eof()){
			inFile>>CharIn;
			index = CharIn;
			hist[index]++;
			count++;
		}
		PrintHistToOutput(hist, outFile, count);
	}
	
	void CreateHistogramTable(ifstream &inFile, ofstream &outFile){
		int hist [256]={0}; double count=0;
		
		FillingHistogramTable(inFile, outFile, hist, count);
	}
	
	
int main(int argc, char* argv[]){
	List link;string charIn;int probIn;Node* newnode;Node* root;string code = "";int N=0;

	if(argc<3){
		cout<<"usage: "<< argv[0]<<" <input filename> <output filename>\n";
		return 0;
	}	

	ifstream inFile (argv[1]);
	inFile>>noskipws;
	ofstream outFile(argv[2]);
	
	CreateHistogramTable(inFile, outFile);
	
	ifstream inFile2 (argv[2]);
	ofstream outFile2 (argv[3]);
	
	while(!inFile2.eof()){
		inFile2>>charIn;
		inFile2>>probIn;
		newnode = new Node(charIn,probIn);
		link.HuffmanLinkedList(newnode);
		N++;
	}
	
	link.print_list(outFile2);
	outFile2<<"Huffman Data In A Binary Tree"<<endl<<endl;
	link.HuffmanBinaryTree(outFile2);
	root=link.getHead()->next;
	link.HuffmanCode(root,code);
	outFile2<<"Entropy Table"<<endl;
	outFile2<<"Char\tProb\tCode     \t\tBits\tEntropy"<<endl;
	link.EntropyTable(outFile2,root,N);
	
	ifstream inFile3 (argv[4]);
	ofstream outFile3 (argv[5]);
	
	link.encode(inFile3, outFile3, N);
	
	ifstream inFile4 (argv[5]);
	ofstream outFile4 (argv[6]);
	
	link.decode(inFile4, outFile4, N);
	
	inFile.close();
	outFile.close();
}
