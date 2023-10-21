// CID: 01333098
// Degree: MSc Computing Science
// Module: 517 Object Oriented Design and Programming
//
// Add all of your code that pertains to question 2 to this file.
#include<sstream> 
#include<iomanip>
#include<iostream>
#include<string>
#include<vector>
using namespace std;
// The declarations of the class template ’vector’ are below.
// template <typename T> class vector {
//     public:
//     vector(); // constructor that creates an empty vector
//     void push_back(const T& item); // adds item to the vector
//     vector<T>::constant_iterator cbegin(); // returns constant iterator
//     vector<T>::constant_iterator cend(); // returns constant iterator
//     unsigned int size(); // returns the number of items
// };
// Available helper functions that can be used
/* Returns a std::string that is comprised of the given symbol with the given
 * length. E.g. makeErrorString(’#’, 3) returns the string: "###" */
std::string makeErrorString(char symbol, unsigned int length) {
    return string(length, symbol);
}
/* Returns a std::string of the given number with the given decimal places. It
 * also rounds numbers up or down as appropriate. E.g. numberToString(10.9, 0)
 * returns the string: "11" */
std::string numberToString(float number, unsigned int decimal_places) {
    stringstream stream;
    stream << std::fixed << std::setprecision(decimal_places) << number;
    return stream.str();
}
/* Add your code below this line. */
// Question a
class CellType {
    int width;
public:
    virtual string contents() = 0;
};

class TextCell: public CellType {
    string text;
public:
    TextCell(string text): text(text) {};
    string contents() override {
        return text;
    }
};

class CurrencyCell: public CellType {
    float amount;
    string currency;
    int decimalPlaces;
public:
    CurrencyCell(float a, string c, int d): amount(a), currency(c), decimalPlaces(d) {};
    string contents() override {
        return numberToString(amount, decimalPlaces) + " " + currency;
    }
};

class DurationCell: public CellType {
    int minutes;
public: 
    DurationCell(int minutes): minutes(minutes) {};
    string contents() override {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return to_string(hours) + ":" + to_string(mins);
    }
};

template<char ERROR_CHAR>
class Table {
    int rows, cols;
    int charactersPerCell;
    std::vector<CellType*> tableContents;
public:
    Table(int rows, int cols, int chars): rows(rows), cols(cols), charactersPerCell(chars) {};
    void addCellToTable(CellType* cell) {
        tableContents.push_back(cell);
    }
    void printTable() {
        int count = 0;
        for (int j=0; j<cols; ++j) {
            for (int i=0; i<rows; ++i) {
                string line = tableContents[count++]->contents();
                int diff = charactersPerCell-line.length();
                if (diff<0) {
                    line = makeErrorString(ERROR_CHAR, charactersPerCell);
                } else {
                    string whitespace = makeErrorString(' ',diff);
                    line = whitespace + line;
                }
                cout << "|" << line << "|";
            }
            cout << endl;
        }
    }
};
// Question b
void testFunction();
int main() {
    // cout << makeErrorString('#',5) << endl;
    // cout << numberToString(5.426231,2) << endl;

    CellType* cell1 = new TextCell("Karting Time:");
    CellType* cell2 = new TextCell("Price:");
    CellType* cell3 = new DurationCell(85);
    CellType* cell4 = new CurrencyCell(60.5, "GBP", 2);
    CellType* cell5 = new DurationCell(25);
    CellType* cell6 = new CurrencyCell(25.4, "USD", 0);

    Table<'#'> table(2,3,13);
    table.addCellToTable(cell1);
    table.addCellToTable(cell2);
    table.addCellToTable(cell3);
    table.addCellToTable(cell4);
    table.addCellToTable(cell5);
    table.addCellToTable(cell6);
    
    table.printTable();
}