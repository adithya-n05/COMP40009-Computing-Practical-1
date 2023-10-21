// Degree: MSc Computing
// Module: 70036 Object Oriented Design and Programming
//
// Add all of your code that pertains to question 2 to this file.

#include<iostream>
#include<string>
#include<vector>
#include<sstream>

// Some declarations of the class template 'vector' are below.

/*
template <typename T> class vector {
	public:
		vector(); // constructor that creates an empty vector
		void push_back(T&& item); // adds an item to the end
		void pop_back(); // removes the last item
		T& back(); // returns a reference to the last item
		vector<T>::constant_iterator cbegin(); // returns constant iterator
		vector<T>::constant_iterator cend(); // returns constant iterator
		bool empty(); // returns true if vector is empty
};
*/

/* Add your code below this line. */

// Question a
using namespace std;

class Vial {

protected:

    string name;
    double liquid;
    double dose;
    double dilution = 0;
public:
    Vial(string name, double liquid, double dose) : name(name), liquid(liquid), dose(dose) {}

    void dilute(double amount) {
        dilution += amount;
        liquid += amount;
    }

    virtual bool administer() {
        if (dose <= liquid) {
            dilution *= dose / liquid;
            liquid -= dose;
            cout << "Administered dose of " << name << endl;
            return true;
        }
        return false;
    }

    virtual string getInfo() {
        stringstream sstream;
        sstream << name << " Vaccine in vial: " << liquid << "ml with dose " << dose << "ml";
        return sstream.str();
    }
};

class VialA : public Vial {
public:
    VialA(string name, double liquid, double dose, double reqDilution) : Vial(name, liquid, dose),
                                                                         reqDilution(reqDilution) {}

    bool administer() override final {
        if ((dilution - reqDilution) < 0.001 && (reqDilution - dilution) < 0.001) {
            bool admin = Vial::administer();
            if (admin) {
                reqDilution *= dose / (liquid + dose);
            }
            return admin;
        } else {
            cout << "Incorrect dilution" << endl;
            return false;
        }
    }

    string getInfo() override final {
        string base = Vial::getInfo();
        stringstream sstream;
        sstream << "Type A; " << base << " of that " << reqDilution << " dilution required";
        return sstream.str();
    }

private:
    double reqDilution;

};

class VialB : public Vial {
public:
    VialB(string name, double liquid, double dose, int maxTemp) : Vial(name, liquid, dose), maxTemp(maxTemp) {}

    bool administer() override final {
        // if(getTemp()>maxTemp){
        //      cout << "Too hot to administer" << endl;
        //      return false;
        // }
        return Vial::administer();
    }

    string getInfo() override final {
        string base = Vial::getInfo();
        stringstream sstream;
        sstream << "Type B; " << base << " with a max temperature of " << maxTemp << " C";
        return sstream.str();
    }

private:
    double maxTemp;
};

template<int SIZE>
class Tray {
private:
    vector<Vial*> tray;

public:

    void add(Vial* vial) {
        if (tray.size() < SIZE)
            tray.push_back(vial);
        else
            cout << "Tray is full" << endl;
    }

    Vial* get() {
        Vial* item = tray.back();
        tray.pop_back();
        return item;
    }

    void print() {
        for (auto it = tray.begin(); it != tray.end(); it++)
            cout << (*it)->getInfo() << endl;
    }

    virtual ~Tray() {
        for (auto it = tray.begin(); it != tray.end(); it++)
            delete *it;
    }
};

// Question b

int main() {
    VialA* protect = new VialA("Covid-Protect", 0.45, 0.3, 1.8);
    VialB* begone = new VialB("Covid-Begone", 5.0, 0.3, 15);
    protect->dilute(1.1);

    Tray<10> tray;
    tray.add(protect);
    tray.add(begone);
    tray.print();

    Vial* vial = tray.get();
    while (vial->administer()) {}
    delete vial;
    tray.print();

    vial = tray.get();
    while (vial->administer()) {}
    delete vial;
    tray.print();
    return 0;

}
