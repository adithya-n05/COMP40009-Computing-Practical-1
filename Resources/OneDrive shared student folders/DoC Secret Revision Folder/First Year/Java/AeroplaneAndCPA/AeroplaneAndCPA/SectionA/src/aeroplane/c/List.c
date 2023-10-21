#include "List.h"
#include <stdio.h>
#include <stdlib.h>


List *create_list(){
    List *list = calloc(1, sizeof(List));
    if (list == NULL){
        perror("error whilst making list");
        exit(EXIT_FAILURE);
    }
    return list;
}

List_Elem *create_elem(){
    List_Elem *elem = calloc(1, sizeof(List_Elem));
    if (elem == NULL){
        perror("error whilst making node");
        exit(EXIT_FAILURE);
    }
    return elem;
}

void destroy_list(List *list){
    List_Elem *elem = list->first;
    for (; elem ; elem = elem->next) {
        free(elem->prev);
    }
    free(list);
}

void add_elem_front(List *list, struct List_Elem *elem){
    if (list->first == NULL){
        list->first = elem;
        list->last = elem;
    } else {
        elem->next = list->first;
        list->first->prev = elem;
        list->first = elem;

    }
}

List_Elem *get_next(List_Elem *elem){
    return elem->next == NULL ? elem : elem->next;
}

List_Elem *get_prev(List_Elem *elem){
    return elem->prev == NULL ? elem : elem->prev;
}

void print_list(List *list){
    if (list->first == NULL){
        printf("Empty list");
        printf("\n\n");
        return;
    }
    int i = 0;
    for (List_Elem *curr = list->first; curr ; curr = curr->next) {
        printf("String at index %d --> %s \n", i, curr->content);
        i++;
    }
    printf("\n");
}

