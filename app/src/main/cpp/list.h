#ifndef __list_h__
#define __list_h__

#include <stdio.h>
#include <stdlib.h>

typedef struct list_head {
    struct list_head *next;
} list_head_t;

#ifndef LIST_HEAD
#define LIST_HEAD(name) \
    struct list_head name = {NULL}
#endif

#ifndef INIT_LIST_HEAD
#define INIT_LIST_HEAD(head) \
    (head)->next = NULL
#endif

#ifndef list_for_each
#define list_for_each(pos, head) \
    for ((pos) = (head)->next; (pos) != NULL; (pos) = (pos)->next)
#endif

#ifndef container_of
#define container_of(ptr, type, menber) \
    (type *)((char*)ptr - (char*) &(((type *)0)->menber))
#endif

#ifndef __list_del
#define __list_del(_req, _head, _type) \
    do { \
        list_head_t *_pos = (_head); \
        while (_pos != NULL) { \
            _type *_node = container_of(_pos->next, _type, list); \
            if (_node->req == (_req)) { \
                _pos->next = _pos->next->next; \
                free(_node); \
                break; \
            } \
            _pos = _pos->next; \
        } \
    } \
    while(0)
#endif


#ifndef dump_list
#define dump_list(_head, _type) \
do { \
    int _num = 0; \
    list_head_t *_pos; \
    list_for_each(_pos, (_head)) { \
        _type *_node = container_of(_pos, _type, list); \
        log("[dump_list]: _num=%d,0x%lx, %s", ++_num, (long)_node, toString(_node->req).c_str()); \
    } \
    if (_num == 0) { \
        log("[dump_list]: empty list."); \
    } \
} \
while(0)
#endif

#ifdef __cplusplus
extern "C" {
#endif
    int list_add(list_head_t *new_node, list_head_t *head);
    int list_add_tail(list_head_t *new_node, list_head_t *head);
#ifdef __cplusplus
}
#endif

#endif







