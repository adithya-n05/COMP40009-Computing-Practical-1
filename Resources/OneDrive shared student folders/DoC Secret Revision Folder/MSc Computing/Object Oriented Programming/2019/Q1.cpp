/* Add ALL of your code to this file. */
#include <string>
#include <utility>
#include <map>
#include <iostream>
using namespace std;
/* Add your code below this line. */

// Solution using the suggested map container.


class NewsItem;
class Source;
class Category;
class Author;
map<string, Category> categories;
map<string, Source> sources;
map<string, Author> authors;


// Question a

class Category {

private:
    const string name;
    string parentName;
    int totalClicks =0;

public:
    Category(const string &name) : name(name){};
    const string &getName() const {
        return name;
    }
    void increaseClicks() {
        totalClicks++;
    }
    void setParent(const string& pName) {
        parentName = pName;
    }

    const Category& getParent() const {
        return categories.at(parentName);
    }
    int getTotalClicks() const {
        return totalClicks;
    }
};

class Author {
public:
    Author(const string &name) : name(name) {}
private:
    string name;
};

class NewsItem {
public:
    NewsItem(const string &title, const string &content, const string &author, const string categoryName)
            : title(title), content(content), authorName(author), categoryName(categoryName) {};
    int getClicks() const {
        return clicks;
    }
    const string& getContent() {
        categories.at(categoryName).increaseClicks();
        clicks++;
        return content;
    }
    const string& getTitle() {
        return title;
    }

private:
    const string title;
    const string content;
    const string categoryName;
    //Name because we assume authors are not owned, and stored in map. Not implemented but would work the same as the sources and categories
    const string authorName;
    int clicks = 0;

};

class Source {
public:
    Source(const string &url) : URL(url) {}

    void addNewsItem(const string &title, const string &text, const string &author, const string& categoryName) {
        if (count == 1000) {
            return; //normally throw but no time
        }
        if (categories.count(categoryName) == 0) {
            Category newCat = Category(categoryName);
            categories.insert(pair<string, Category>(categoryName, newCat));
        }
        items[count] = new NewsItem(title, text, author, categoryName);
        count++;
    }

    NewsItem& getItemWithTitle(const string& title) {
        NewsItem* ptr = nullptr;
        for (NewsItem* item : items) {
            if (item) {
                if (item->getTitle() == title) {
                    ptr = item;
                }
            }
        }
        //Will segfault if no item, same as Roasty.
        return *ptr;
    }

    //Should have copy constructor and assignment overload, but lazy.

    ~Source() {
        for (NewsItem* item : items) {
            if (item) delete item;
        }
    }

private:
    const string URL;
    NewsItem* items[1000] = {};
    int count = 0;
};


// Question b

 void insertNewsItem(const string &title, const string &text, const string &author, const string& categoryName, const string &sourceWebpage) {
     if (sources.count(sourceWebpage)==0) {
         Source newSrc = Source(sourceWebpage);
         sources.insert(pair<string,Source>(sourceWebpage,newSrc)); //Doesn't need copy constructor since News items list always starts empty in this use case. Normally would need it.
     }
     sources.at(sourceWebpage).addNewsItem(title, text, author, categoryName);
 }


// Question c

const string& getContent(const string& sourceURL, const string& title) {
     return sources.at(sourceURL).getItemWithTitle(title).getContent();
 }


// Question d

const string getHottestCategory() {
     int max = 0;
     string catName;
    for (auto const& src : categories)
    {
        if (src.second.getTotalClicks() > max) {
            max = src.second.getTotalClicks();
            catName = src.first;
        }
    }
    return catName;
 };

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
    cout << content << endl;
}
void usage3() { //
    auto categoryName = getHottestCategory();
    cout << categoryName << endl;
}
int main(int argc, char* argv[]) {
    usage1();
    usage2();
    usage3();
    return 0;
}