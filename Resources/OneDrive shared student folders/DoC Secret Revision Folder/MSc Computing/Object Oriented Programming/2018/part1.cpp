#include<iostream>
#include<string>
using namespace std;


class Pizza{
    private:
    string name;
    int weight;
    bool cooked=false;

    public:
    Pizza(string name, int weight) : name(name), weight(weight) {}
    int getWeight() { return weight;}
    void cook(){
        if(!cooked){
            weight /=0.85;
            cooked = true;
        }
    }
};

class Drone{
    private:
    float lat;
    float lon;
    float alt;
    float cruising_alt;
    Pizza* load=nullptr;

    public:
    Drone(float lat, float lon, float alt, float cruising_alt=100) : lat(lat), lon(lon), alt(alt), cruising_alt(cruising_alt) {}
    Drone(Drone const& old) {*this=old;}
    ~Drone(){
        delete load;
    }
    Drone& operator=(Drone const& old){
        this->lat = lat;
        this->lon = lon;
        this->alt = alt;
        this->cruising_alt = cruising_alt;
        this->load = new Pizza(*old.load);
        return *this;
    }

    void load_pizza(Pizza* load) {
        if(load->getWeight() <= 500)
            this->load=load;
    }

    Pizza* release() {
        Pizza* ret = load;
        load = nullptr;
        return ret;
    }
    void fly_to(float lat, float lon, float alt){
        this->alt = cruising_alt;
        this->lat = lat;
        this->lon = lon;
        this->alt = alt;
        return;
    }
};

int main(){
    Pizza* pizza = new Pizza("Margherita", 512);
    pizza->cook();

    Drone drone(51.4988, -0.1749, 0.);
    drone.load_pizza(pizza);
    drone.fly_to(51.5010,-0.1919, 31.5);
    Pizza* del_pizza = drone.release();
    drone.fly_to(51.4988, -0.1749, 0);

    delete del_pizza;

    return 0;
}