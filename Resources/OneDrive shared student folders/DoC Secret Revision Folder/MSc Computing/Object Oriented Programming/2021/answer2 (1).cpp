// Degree: MSc Computing
// Module: 70036 Object Oriented Design and Programming
//
// Add all of your code that pertains to question 2 to this file.

#include<iostream>
#include<string>
#include<vector>
#include<iomanip>

// Some declarations of the class template 'vector' are below.


// template <typename T> class vector {
// public:
//   vector(); // constructor that creates an empty vector
//   void push_back(T&& item); // adds an item to the end
//   void pop_back(); // removes the last item
//   T& back(); // returns a reference to the last item
//   vector<T>::constant_iterator cbegin(); // returns constant iterator
//   vector<T>::constant_iterator cend(); // returns constant iterator
//   bool empty(); // returns true if vector is empty
// };


/* Add your code below this line. */

// Question a
double temp() {
  return 10.0;
}

class Vaccine{
public:
  double dose;  
  double amount;
  std::string name;

  Vaccine(double amount, double dose, std::string name) :
  amount(amount), dose(dose), name(name) {};
  virtual ~Vaccine() {};
  virtual void print() = 0;
  double const& getAmount() const {
    return amount;
  }
  virtual bool safeToAdminister() const = 0;
  virtual void addLiquid(double amount) = 0;
  void administer(){
    if(safeToAdminister()){
      amount -= dose;
      std::cout << "\nAdministered " << dose << "ml of " << name << '\n';
    }
  }
};

class TypeA : public Vaccine{
  double dilution_amount;
  bool diluted = false;
public:
  TypeA(double amount, double dose, double dilution_amount,
	std::string name) : Vaccine(amount, dose, name), 
  dilution_amount(dilution_amount) {};
  void print() override {
    std::cout << "Name:" << name
	      << "\nDilution Amount(ml):"
	      << dilution_amount << '\n';
  }
  bool safeToAdminister() const override {
    return diluted && amount > dose;
  }
  void addLiquid(double amount){
    if(!diluted && amount == dilution_amount){
      diluted = true;
    } else {
      std::cout << "Incorrect amount\n";
    }
  }
};

class TypeB : public Vaccine{
  double safe_temp = 20.0;
public:
  TypeB(double amount, double dose, double safe_temp,
	std::string name) : Vaccine(amount, dose, name),
			    safe_temp(safe_temp) {};
  void print() override {
    std::cout << "Name:" << name
	      << "\nSafe Temperature:"
	      << safe_temp << '\n';
  }
  void addLiquid(double amount) override {
    std::cout << "Cannot add liquid to this vaccine\n";
  }
  bool safeToAdminister() const {
    return temp() < safe_temp && amount > dose;
  }
};



class Vial{
  Vaccine& vaccine;
  double capacity;
public:
  Vial(double capacity, Vaccine& vaccine) : capacity(capacity), vaccine(vaccine) {};
  void addLiquid(double amount){
      vaccine.addLiquid(amount);
  }
  void print() const {
    double amount_used = 0;
      vaccine.print();
      amount_used = vaccine.getAmount();
      std::cout << "Vaccine Quantity : " << amount_used
                << "\nVial capacity left: "
	              << capacity - amount_used << '\n';
  }
  bool safeToAdminster() const {
    return vaccine.safeToAdminister();
  }
  void administer(){
    vaccine.administer();
  }
};

template <int MAX_OBJECTS> class Tray {
  std::vector<Vial> vials;
public:
  void addVial(Vial vial){
    if(vials.cend() - vials.cbegin() < MAX_OBJECTS){
      vials.push_back(vial);
      return;
    }
    std::cout << "Tray is full\n";    
  }
  void print() const { 
    std::cout << "\n\nTray Status:\n";
    for(Vial vial : vials){
      vial.print();
    } 
  };
  void administerVaccines(){
    for(Vial vial : vials){
      while(vial.safeToAdminster()){
	      print();
	      vial.administer();
      }
    }
  }
};
  

// Question b

int main() {
  std::cout << std::setprecision(2) << std::fixed; //just to make output more readable

  TypeA protect = TypeA(0.45, 0.3, 1.8, "Covid-Protect");
  TypeB begone = TypeB(5.0, 0.5, 15.0, "Covid-Begone");
  Vial vial1 = Vial(3.0, protect);
  Vial vial2 = Vial(6.0, begone);

  vial1.addLiquid(1.1);
  vial1.addLiquid(1.8); //just to make output more interesting
  Tray<10> tray = Tray<10>();
  tray.addVial(vial1);
  tray.addVial(vial2);

  tray.administerVaccines();

  return 0;
}
