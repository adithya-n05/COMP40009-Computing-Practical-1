#include <stdio.h>
#include "List.h"

int main() {
    List *list = create_list();
    List_Elem  *elem = create_elem();
    char *f = "hello";
    char *s = "hola";
    elem->content = f;


    List_Elem *elem1 = create_elem();
    elem1->content = s;

    print_list(list);
    add_elem_front(list, elem);
    print_list(list);
    add_elem_front(list, elem1);
    print_list(list);
    destroy_list(list);
    return 0;
}
