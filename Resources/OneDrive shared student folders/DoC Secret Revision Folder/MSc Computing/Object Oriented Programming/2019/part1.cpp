/* Add ALL of your code to this file. */
#include <string>
#include <utility>
#include<map>
#include<exception>
#include<iostream>
using namespace std;

/* Add your code below this line. */
// Question a
class Category;

class News{
    public:
    string const title;
    string const content;
    string const author;
    Category& category;
    unsigned int views =0;
    News(string title, string content, string author, Category& category) : title(title), content(content), author(author), category(category) {}
};

class Source{
    private:
    News* news[1000];
    bool operator=(Source const&) {throw std::logic_error("Not implemented.");}
    Source(Source const&) {throw std::logic_error("Not implemented.");}

    public:
    string const name;
    Source(string name) : news(), name(name) {}
    ~Source() {
        for(int i=0;i<1000;i++)
            delete news[i];
    }
    void addNews(string name, string content, string author, Category& category);
    string getNews(string name);
    unsigned int getClicksByCategory(Category& cat);
};

class Category{
    public:
    string const name;
    Category const* parent;
    Category(string name, Category* parent=nullptr) : name(name), parent(parent) {}    
};

class DB{
    public:
    Source* sources[100];
    Category* categories[500];

    DB() : sources(), categories() {}
    ~DB() {
        for(int i=0;i<100;i++)
            delete sources[i];
        for(int i=0;i<500;i++)
            delete categories[i];
    }
    void insertNewsItem(string title, string content, string author, string categoryName, string sourceWebpage);
    Source* getSource(string sourceWebpage, bool addSource=true);
    Category* getCategory(string categoryName);
    string getContent(string sourceWebpage, string title);
    string getHottestCategory();
};

DB db;

// Question b
void insertNewsItem(string title, string content, string author, string categoryName, string sourceWebpage){
    db.insertNewsItem(title, content, author, categoryName, sourceWebpage);
}

void DB::insertNewsItem(string title, string content, string author, string categoryName, string sourceWebpage){
    Source* source = getSource(sourceWebpage);
    Category* category  = getCategory(categoryName);
    source->addNews(title, content, author, *category);

}

Source* DB::getSource(string sourceWebpage, bool addSource){
    for(int i=0;i<100;i++){
        if(sources[i]!=nullptr){
            if(sources[i]->name == sourceWebpage)
                return sources[i];
        }
    }
    if(addSource==false)
        throw runtime_error("Source does not exist");
    for(int i=0;i<100;i++){
        if(sources[i]==nullptr){
            sources[i] = new Source(sourceWebpage);
            return sources[i];
        }
    }
    throw runtime_error("Failed to get source, out of memory");
}

Category* DB::getCategory(string categoryName){
    for(int i=0;i<100;i++){
        if(categories[i]!=nullptr){
            if(categories[i]->name == categoryName)
                return categories[i];
        }
    }
    for(int i=0;i<100;i++){
        if(categories[i]==nullptr){
            categories[i] = new Category(categoryName);
            return categories[i];
        }
    }
    throw runtime_error("Failed to get category, out of memory");
}

void Source::addNews(string name, string content, string author, Category& category){
    for(int i=0;i<100;i++){
        if(news[i]==nullptr){
            news[i] = new News(name, content, author, category);
            return;
        }
    }
    throw runtime_error("Failed to add news, out of memory");

}
// Question c
string getContent(string sourceWebpage, string title){
    return db.getContent(sourceWebpage, title);
}

string DB::getContent(string sourceWebpage, string title){
    Source* source = getSource(sourceWebpage, false);
    return source->getNews(title);
}


string Source::getNews(string name){
    for(int i=0;i<100;i++){
        if(news[i]!=nullptr){
            if(news[i]->title == name){
                news[i]->views++;
                return news[i]->content;
            }
        }
    }
    throw runtime_error("Failed to get news");
}
// Question d

string getHottestCategory(){
    return db.getHottestCategory();

}

string DB::getHottestCategory(){
    int hottest = 0;
    string hottest_name;
    for(int i=0;i<500;i++){
        if(categories[i]!=nullptr){
            int clicks =0;
            for(int i=0;i<100;i++){
                if(sources[i]!=nullptr){
                    clicks += sources[i]->getClicksByCategory(*categories[i]);
                }
            }
            if(clicks>=hottest)
                hottest_name = categories[i]->name;
        }
    }
    return hottest_name;


}

unsigned int Source::getClicksByCategory(Category& cat){
    unsigned int clicks = 0;
    for(int i=0;i<100;i++){
        if(news[i]!=nullptr){
            if(news[i]->category.name == cat.name){
                clicks += news[i]->views;
            }
        }
    }
    return clicks;

}

/* Here are examples of how the functions should be used (copied from
exam paper). */
/* DO NOT CHANGE ANYTHING BELOW THIS LINE!!! */
void usage1() {
    auto title = "Something Happened";
    auto text = "London is a city where things happen all the time";
    auto author = "A random stranger";
    auto categoryName = "London News";
    auto sourceWebpage = "http://www.blameberg.com";
    insertNewsItem(title, text, author, categoryName, sourceWebpage);
}

void usage2() {
    auto sourceWebpage = "http://www.blameberg.com";
    auto title = "Something Happened";
    string content = getContent(sourceWebpage, title);
}

void usage3() { //
    auto categoryName = getHottestCategory();
}

int main(int argc, char* argv[]) {
    usage1();
    usage2();
    usage3();
    return 0;
}