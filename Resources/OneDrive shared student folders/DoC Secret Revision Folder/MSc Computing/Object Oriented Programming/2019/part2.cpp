/* Add ALL of your code to this file. */
/* The declarations of the class template ’set’ are below. Do NOT uncomment them.
template <typename T> class set {
public:
set(); // constructor that creates an empty set
void insert(const T& item); // adds item to the set
set<T>::constant_iterator begin(); // returns constant iterator
set<T>::constant_iterator end(); // returns constant iterator
void erase(set<T>::const_iterator); // removes specified item
bool empty(); // returns true if the set is empty
};
*/
/* Do NOT add any other header files. */
#include<iostream>
#include<string>
#include<set>
/* Add your code below this line. */
using namespace std; // Save time
// Question b

class Fruit{
    protected:
    string type;
    int weight;
    int date;
    string variety;

    public:
    Fruit(string type, int weight, int date, string variety) : type(type), weight(weight), date(date), variety(variety) {}
    virtual ~Fruit() {}
    virtual string printFruit(){
        string txt = "Type: ";
        txt += type;
        txt += "; Variety ";
        txt += variety;
        txt += "; Weight ";
        txt +=  to_string(weight);
        txt += "g; Best Before ";
        txt += to_string(date);
        txt += "days";
        return txt;
    }
    virtual bool qualifiesPremium() = 0;
    bool fitForSale() {
        return (date>=3);
    }
};

class Apple: public Fruit {
    protected: 
    int redPercentage;
    int const min_weight = 90;
    float const minRed;

    public:
    Apple(string type, int weight, int date, string variety, float redPercentage, float minRed) : Fruit(type, weight, date, variety), redPercentage(redPercentage), minRed(minRed) {}
    string printFruit() override final{
        string txt = Fruit::printFruit();
        txt += "; Red Surface ";
        txt += to_string(redPercentage);
        txt += "%";
        return txt;
    }
    virtual ~Apple() {}
    bool qualifiesPremium() override final {
        bool w = (weight>=min_weight) ? true : false;
        bool c = (redPercentage>=minRed) ? true : false;
        return w && c;
    }
};

class RedApple: public Apple{
    public:
    RedApple(int weight, int date, string variety, float redPercentage) : Apple("Red Apple", weight, date, variety, redPercentage, 75.) {}
};

class MixedApple: public Apple{
    public:
    MixedApple(int weight, int date, string variety, float redPercentage) : Apple("Mixed Apple", weight, date, variety, redPercentage, 50.) {}
};

class Pear: public Fruit {
    private:
    bool stalkIntact;

    public:
    Pear(int weight, int date, string variety, bool stalkIntact) : Fruit("Pear", weight, date, variety), stalkIntact(stalkIntact) {}
    string printFruit() override final{
        string txt = Fruit::printFruit();
        txt += "; Stalk Intact ";
        txt += stalkIntact ? "True" : "False";
        return txt;
    }
    bool qualifiesPremium() override final {
        return stalkIntact;
    }

};


template<typename T, int CAPCACITY>
class Basket{
    private:
    T* basket[CAPCACITY];

    public:
    Basket() : basket() {}
    ~Basket() {
        for(int i=0;i<CAPCACITY;i++)
            delete basket[i];
    }
    void addItem(T* item){
        for(int i=0;i<CAPCACITY;i++){
            if(basket[i]==nullptr){
                basket[i] = item;
                return;
            }
        }

    }
    T* removeItem(){
        for(int i=0;i<CAPCACITY;i++){
            if(basket[i]!=nullptr){
                T* item = basket[i];
                basket[i] = nullptr;
                return item;
            }
        }
        return nullptr;
    }
    void printBasket(){
        for(int i=0;i<CAPCACITY;i++){
            if(basket[i]!=nullptr){
                cout << basket[i]->printFruit() << endl;
            }
        }
    }

};



// Question c
int main() {
    Basket<Fruit, 30> basket;
    basket.addItem(new RedApple(95, 7, "Gala", 80.));
    basket.addItem(new MixedApple(80, 8, "Cox", 60.));
    basket.addItem(new Pear(140, 2, "Ambrosia", true));
    basket.printBasket();

    Basket<Fruit, 10> basket2, basket3;

    while(true){
        Fruit* item = basket.removeItem();
        if(item==nullptr)
            break;
        if(item->fitForSale() && item->qualifiesPremium())
            basket2.addItem(item);
        else
            basket3.addItem(item);
    }
    cout << "Basket 2" << endl;
    basket2.printBasket();
    cout << "Basket 3" << endl;
    basket3.printBasket();


}