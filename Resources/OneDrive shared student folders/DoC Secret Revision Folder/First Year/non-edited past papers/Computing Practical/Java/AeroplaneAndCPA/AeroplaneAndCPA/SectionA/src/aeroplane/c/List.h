#ifndef DOUBLY_LINKED_LIST_H
#define DOUBLY_LINKED_LIST_H

typedef  struct List_Elem{
    char *content;
   struct List_Elem *prev;
   struct List_Elem *next;

} List_Elem;

typedef struct List{
    struct List_Elem *first;
    struct List_Elem *last;
}List;

//creates list
List *create_list();

// destroys list and frees memory
void destroy_list(List *list);

//creates Element
List_Elem *create_elem();

void add_elem_front(List *list, struct List_Elem *elem);

List_Elem *get_next(List_Elem *elem);

List_Elem *get_prev(List_Elem *elem);

void print_list(List *list);

#endif //DOUBLY_LINKED_LIST_H
