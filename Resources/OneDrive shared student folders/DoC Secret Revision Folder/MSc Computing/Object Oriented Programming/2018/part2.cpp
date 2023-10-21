#include<iostream>
#include<string>
#include<list>
#include<algorithm>
using namespace std;

enum Type {heading, paragraph};
int const MAX_LENGTH = 1000;

int line_breaks(string s){
    return std::count(s.begin(), s.end(), '\n');
}


class Sheet{
    public:
    Sheet(int const width, int const height) : width(width), height(height) {}
    int width;
    int height;
};

struct Text{
    public:
    string txt;
    Type type;
};

class Leaflet{
    private:
    int id;
    list<Text> elements; 
    int length = 0;
    Sheet sheet;
    
    public:
    Leaflet(int id) : id(id), sheet(80, 20) {}

    void setSheet(Sheet const& new_sheet) {sheet = new_sheet;}


    void addElement(Text elem) {
        if(length+elem.txt.length() > MAX_LENGTH){
            cout << "Failed to add element, max length exeeded" << endl;
            return;
        }
        length += elem.txt.length();
        elements.push_back(elem);
    }

    void printLeaflet(){
        int i=1;
        for(auto it=elements.begin();it!=elements.end();it++){
            string msg;
            if(it->type==heading){
                msg =  to_string(i++);
                msg += ". ";
                msg += it->txt;
                msg += "\n";
            } else {
                msg = "  ";
                msg += it->txt;
                msg += "\n\n";
            }
            printBlock(msg);
        }
    }

    void printBlock(string msg){
        for(unsigned int i=sheet.width;i<msg.length();i+=(sheet.width+1)){
            msg.insert(i, "\n");
        }
        cout << msg;
        if(line_breaks(msg) > sheet.height+1)
            cout << "Warning, exeeded height" << endl;
    }

};




int main(){
    Leaflet first(1);
    Sheet small(50,50);
    Sheet tiny(20,40);

    first.addElement({.txt = "Importance of Bees", .type=heading});
    first.addElement({.txt = "Bees are important because ...", .type=paragraph});
    first.addElement({.txt = "Threats to Bees", .type=heading});
    first.addElement({.txt = "Significant threats ...", .type=paragraph});

    first.setSheet(small);
    first.printLeaflet();
    first.setSheet(tiny);
    first.printLeaflet();

    return 0;
}