// CID:
// Degree: MSc Computing Science
// Module: 517 Object Oriented Design and Programming
//
// Add all of your answers to question 2 to this file.

#include<iostream>
#include<string>
#include<vector>
#include<iomanip>
#include<sstream>

// The declarations of the class template 'vector' are below.

/*
template <typename T> class vector {
	public:
		vector(); // constructor that creates an empty vector
		void push_back(const T& item); // adds item to the vector
		vector<T>::constant_iterator cbegin(); // returns constant iterator
		vector<T>::constant_iterator cend(); // returns constant iterator
		unsigned int size(); // returns the number of items
};
*/

// Available helper functions that can be used

/* Returns a std::string that is comprised of the given symbol with the given
 * length. E.g. makeErrorString('#', 3) returns the string: "###" */

std::string makeErrorString(char symbol, unsigned int length) {
	return std::string(length, symbol);
}

/* Returns a std::string of the given number with the given decimal places. It
 * also rounds numbers up or down as appropriate. E.g. numberToString(10.9, 0)
 * returns the string: "11" */

std::string numberToString(float number, unsigned int decimal_places) {
	std::stringstream stream;
	stream << std::fixed << std::setprecision(decimal_places) << number;
	return stream.str();
}

/* Add your code below this line. */

// Question a

class Cell {
	private:
	int no_spaces;

	protected:
	std::string getSpaces() { return makeErrorString(' ', no_spaces);}

	public:
	Cell(int const no_spaces) :  no_spaces(no_spaces) {}
	virtual std::string getContent() = 0;
};

class Text: public Cell {
	private:
	std::string text;

	public:
	Text(std::string const& text, int const no_spaces = 0) : Cell(no_spaces), text(text) {}
	std::string getContent() override final {return text;}
};


class Currency: public Cell {
	private:
	float amount;
	unsigned int decimals;
	std::string prefix;

	public:
	Currency(float amount, unsigned int decimals, std::string& prefix, int no_spaces = 0) : 
		Cell(no_spaces), amount(amount), decimals(decimals), prefix(prefix) {}

	std::string getContent() override final {
		std::string text = getSpaces();
		text += numberToString(amount, decimals);
		text +=  prefix;
		return text;
	}
};

class Duration: public Cell {
	private:
	int minutes;

	public:
	Duration(float const minutes, int const no_spaces=0) : Cell(no_spaces), minutes(minutes) {}
	std::string getContent() override final {
		int mins = minutes % 60;
		int hours = minutes / 60; // Note deliberately using int to drop decimals
		std::string text = getSpaces();
		text += std::to_string(hours);
		text += ":";
		text += std::to_string(mins);
		return text;
	}
};

template<char ERROR_CHAR>
class Table{
	private:
	unsigned int const width;
	unsigned int const height;
	unsigned int const cell_width;

	std::vector<std::vector<Cell*>> table;

	public:
	Table(unsigned int const width, unsigned int const height, unsigned int const cell_width) : width(width), height(height), cell_width(cell_width) {}
	void printTable();
	void addCell(Cell* cell);

};

template<char ERROR_CHAR>
void Table<ERROR_CHAR>::printTable() {
	std::vector<std::vector<Cell*>>::iterator it = table.begin();
	for(;it!=table.cend();it++){
		std::vector<Cell*>::iterator it_row = it->begin();
		for(;it_row!=it->cend();it_row++){
			std::cout << "|";
			std::string text =  (**it_row).getContent();
			if(text.length()>cell_width)
				std::cout << makeErrorString(ERROR_CHAR, cell_width);
			else {
				std::cout << text << makeErrorString(' ', cell_width-text.length());
			}
		}
		std::cout << "|" << std::endl;
	}
}

template<char ERROR_CHAR>
void Table<ERROR_CHAR>::addCell(Cell* cell){
	std::vector<std::vector<Cell*>>::iterator it = table.begin();
	for(;it!=table.cend();it++)
		if(it->size() < width){
			it->push_back(cell);
			return;
		}
	if(table.size() >=height){
		std::cout << "Error: Table full" << std::endl; 
		return;
	}
	std::vector<Cell*> new_row;
	new_row.push_back(cell);
	table.push_back(new_row);
}
// Question b

int main() {

	Table<'#'> table(2, 3, 10);

	table.addCell(new Text("Karting Time:"));
	table.addCell(new Text("Price:"));
	table.addCell(new Duration(85));
	table.addCell(new Currency(60.5, 2, "GBP"));
	table.addCell(new Duration(25));
	table.addCell(new Currency(25.4, 0, "USD", 3));

	table.printTable();

}
