
#include <iostream>
#include <list>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;


struct WikiPage {
    Wikipage(int id , string t){
	ID = id ;
	title = t ;
    }
    int ID;
    string title;
    string html_location;
    string txt_location;
};

struct Edge {
    Edge(int o , int d , int w){
	origin = o ;
	destination = d ;
	weight = w;
    }
    int origin;
    int destination;
    int weight;
};

void print(list <Edge> & lst){
    list <Edge> :: const_iterator iterator ;
    for (iterator = lst.begin() ; iterator != lst.end() ; iterator++){
	cout << "{" <<endl ;
	cout << "\t origin " << iterator->origin <<endl;
	cout << "\t destination " << iterator->destination <<endl;
	cout << "\t weight " << iterator->weight <<endl;
	cout << "}" <<endl <<endl;
    }
}

typedef vector<list<Edge> > adjacencyList;

adjacencyList organizeList(list<Edge>& lst, int numberVertices){
    adjacencyList * result = new adjacencyList (numberVertices, list <Edge> ());
    list <Edge> :: const_iterator iterator ;
    //add a copy of the iterator to each destination and origin index 
    for (iterator = lst.begin() ; iterator != lst.end() ; iterator++){
	(*result).at(iterator->destination).push_back(*iterator);
	(*result).at(iterator->origin).push_back(*iterator);
    }
    return *result;
}

typedef map<int,WikiPage> idToWikiMap;

// void printOrganized(adjacencyList& lst , idToWikiMap page_ofID ){

//     list<Edge> listOfEdges;
//     for(int i = 0; i < lst.size(); i ++){
// 	listOfEdges = lst.at(i);
// 	cout << " page :" << page_ofID.at(i+1).title << endl; 
	
// 	//iterate throught the e
// 	list<Edge>::iterator it = listOfEdges.begin();
// 	for(it; it != listOfEdges.end(); it++){
// 	    if(it->origin == i+1){
// 		cout << page_ofID.at(it->destination).title << ":" << it->weight;
// 	    }
// 	    else{
// 		cout << page_ofID.at(it->origin).title << ":" << it->weight;
// 	    }

// 	    if(it != listOfEdges.end()){
// 		cout << ", ";
// 	    }
// 	}
//     }

// }



int main() {
    // your code goes here
    struct Edge *e1 = new Edge(1,2,-1);
    struct Edge *e2 = new Edge(1,3,-1);
    struct Edge *e3 = new Edge(1,4,-1);
    list <Edge> lst;
    lst.push_back(*e1);
    lst.push_back(*e2);
    lst.push_back(*e3);
    print (lst);

    struct WikiPage *w1 = new WikiPage(1,"hi1");	
    struct WikiPage *w2 = new WikiPage(2,"hi2");
    struct WikiPage *w3 = new WikiPage(3,"hi3");
    struct WikiPage *w4 = new WikiPage(4,"hi4");	

    map <int, WikiPage> wiks ;

    wiks[1] = *w1;
    wiks[2] = *w2;
    wiks[3] = *w3;
    wiks[4] = *w4;

    adjacencyList al = organizeList (lst,100);
    //	printOrganized(al,wiks);

    return 0;
}
