#include <stdio.h>
#include <stdlib.h>

#include "list.h"

int list_add(list_head_t *new_node, list_head_t *head)
{
    if (head->next == NULL) {
        head->next = new_node;
        new_node->next = NULL;
    }
    else {
        new_node->next = head->next;
        head->next = new_node;
    }
    return 0;
}

int list_add_tail(list_head_t *new_node, list_head_t *head)
{
    if (head->next == NULL) {
        head->next = new_node;
        new_node->next = NULL;
    }
    else {
        list_head_t *pos;
        list_for_each(pos, head) {
            if (pos->next == NULL) {
                pos->next = new_node;
                new_node->next = NULL;
                break;
            }
        }
        return -1;
    }
    return 0;
}
