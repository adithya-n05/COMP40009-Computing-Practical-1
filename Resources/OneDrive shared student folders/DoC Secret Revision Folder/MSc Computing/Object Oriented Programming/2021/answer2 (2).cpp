#include<iostream>
#include<string>
#include<vector>
// Some declarations of the class template ’vector’ are below.

template <typename T> class vector {
    public:
        vector(); // constructor that creates an empty vector
        void push_back(T&& item); // adds an item to the end
        void pop_back(); // removes the last item
        T& back(); // returns a reference to the last item vector<T>::constant_iterator cbegin(); // returns constant iterator vector<T>::constant_iterator cend(); // returns constant iterator bool empty(); // returns true if vector is empty
};

// Add your code below this line. 
// Question a

float getTemp();
class Vial;
class Vaccine;
class VaccineA;
class VaccineB;
template<int>class Tray;

enum Type {A, B};

class Vial {
    public:
        float capacity;
        float liquid_left;
        Vaccine* vaccine;

        Vial(float capacity, Vaccine* vaccine) : capacity(capacity), vaccine(vaccine) {
            if (vaccine->amount > capacity) {
                throw std::runtime_error("Vaccine amount over vial capacity.");
            }
            liquid_left = vaccine->dose;
        }

        void dilute(float diluted_amount) {
            liquid_left += diluted_amount;
        }

        void print_info() {
            std::cout << "Vaccine: " << vaccine->name << std::endl;
            std::cout << "Dose: " << vaccine->dose << " ml" << std::endl;
            std::cout << "Liquid left: " << liquid_left << " ml" << std::endl;
            // Prints vaccine specific info, i.e. dilution or max temperature
            vaccine->print_specific_info();
        }

        void administer() {
            if (vaccine->checkSafe(liquid_left, getTemp())) {
                print_info();
                if (liquid_left >= vaccine->dose) {
                    liquid_left -= vaccine->dose;
                    std::cout << "Administering " << vaccine->amount << "ml of " 
                    << vaccine->name << std::endl;
                }
            } else {
                std::cout << "Vaccine is not safe to administer" << std::endl;
            }
        }


};

class Vaccine {
    public:
        std::string name;
        float amount;
        float dose;
        Type type;

        Vaccine(std::string name, float amount, float dose, Type type) :
        name(name), amount(amount), dose(dose), type(type) {};

        virtual void print_specific_info() = 0;
        virtual bool checkSafe(float liquid_left, float temperature) = 0;

};

class VaccineA : public Vaccine {
    public:
        float dilution;
        bool diluted;

        VaccineA() : Vaccine("Covid-Protect", 0.45, 0.3, A), dilution(1.8), diluted(false) {};
        void print_specific_info() override {
            std::cout << "Dilution: " << dilution << " ml" << std::endl;
        }
        bool checkSafe(float liquid_left, float temperature) override {
            if (amount + dilution == liquid_left) {
                return true;
            }
            return false;
        }
};

class VaccineB : public Vaccine {
    public:
        int max_temp;

        VaccineB(int max_temp = 20) : Vaccine("Covid-Protect", 5.0, 0.5, B), max_temp(15) {};

        void print_specific_info() override {
            std::cout << "Max Temperature: " << max_temp << " ml" << std::endl;
        }

        bool checkSafe(float liquid_left, float temperature) override {
            if (temperature <= max_temp && amount == liquid_left) {
                return true;
            }
            return false;
        }
};

template<int SIZE>
class Tray {
    private:
        vector<Vial*> tray;
    public:
        void add(Vial* vial) {
            if (tray.size() < SIZE) {
                tray.push_back(vial);
            } else {
                std::cout << "Tray is full";
            }
        }

        bool take_from() {
            if (tray.size() == 0) {
                return false;
            }
            Vial* vial = tray.back();
            vial->administer();
            tray.pop_back();
            return true;
        }
        
};


// Question b

int main() {
    // Create two vials
    VaccineA* vaccineA = new VaccineA();
    VaccineB* vaccineB = new VaccineB(15);
    Vial vial1(3.0f, vaccineA);
    Vial vial2(6.0f, vaccineB);

    // Incorrect dilution
    vial1.dilute(1.1);

    // Create a tray
    Tray<10> tray;
    tray.add(&vial1);
    tray.add(&vial2);

    // Iteration
    while (tray.take_from()) {}
}
