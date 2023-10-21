#include "trie.h"

#include <stdlib.h>
#include <assert.h>
#include <stdio.h>
#include <string.h>

// Returns the position of the bit that represents the existence of this symbol
// in the bitfield, or -1 if the symbol is not in the alphabet we support.
int get_bit_pos(char symbol) {
  if ('A' <= symbol && symbol <= 'Z')
    return 'Z' - symbol + 1;
  else switch (symbol) {
      case '_'  : return 'Z' - 'A' + 2;
      case '.'  : return 'Z' - 'A' + 3;
      case '-'  : return 'Z' - 'A' + 4;
      case '\'' : return 'Z' - 'A' + 5;
      case ' '  : return 'Z' - 'A' + 6;
      default   : return -1;
    }
}

static void print_bits(uint32_t bits) {
    uint32_t mask = 1 << 31;
    int set = 0;
    for (int i = 0; i < 32; i++) {
        if (bits & mask) {
            printf("1");
        } else {
            printf("0");
        }
        bits <<=  1;
    }

}

trie_t *trie_new() {
    trie_t *tree = calloc(sizeof(trie_t), 1);
    return tree;
}

int count_set_bits(uint32_t n) {
    uint32_t mask = 1 << 31;
    int set = 0;
    for (int i = 0; i < 32; i++) {
        if (n & mask) {
            set++;
        }
        n <<= 1;
    }
  return set;
}

int count_children(uint32_t n) {
    int set = count_set_bits(n);
    if (n & 1) {
        set -= 1;
    }
    return set;
}

void trie_free(trie_t *root) {
    if (!root) {
        return;
    }
    int children = count_children(root->bitfield);
    for (int i = 0; i < children; i++) {
        trie_free(root->children[i]);
    }
    free(root->children);
    free(root);

}

static int get_index_in_children(int pos, uint32_t bitfield) {
    uint32_t index_mask =  ~((1 << (pos + 1)) - 1);
    index_mask |= 1 << pos;
    int set = count_set_bits(index_mask & bitfield);
    return set - 1;
}

static bool has_char(const char c, uint32_t bitfield) {
    int pos = get_bit_pos(c);
    if (pos == -1) {
        return false;
    }
    uint32_t mask = 1 << pos;
    return mask & bitfield;
}

static bool is_bit_set(int bit, uint32_t n) {
    return n & (1 << bit);
}

bool trie_get(const trie_t *root, const char *key, int *value_out) {
  assert(root);
  if (!*key) {
    // reached the end, value was found
    bool is_value = 1 & root->bitfield;
    if (!is_value) {
        return false;
    }
    *value_out = root->value;
    return true;
  }
  if (!has_char(*key, root->bitfield)) {
    return false;
  }
  int pos = get_bit_pos(*key);
  if (pos == -1) {
    return false;
  }

  // see the position in the children of *key
  uint32_t mask = ~((1 << (pos + 1)) - 1);
  mask |= 1 << pos;
  int set = count_set_bits(mask & root->bitfield);
  return trie_get(root->children[set - 1], key + 1, value_out);
}

bool trie_insert(trie_t *root, const char *key, int value) {
  assert(root);
  if (!*key) {
      // reached the end
      root->value = value;
      root->bitfield |= 1;
      return true;
  }
  int pos = get_bit_pos(*key);
  if (pos == -1) {
    return false;
  }
  if (is_bit_set(pos, root->bitfield)) {
      int ind = get_index_in_children(pos, root->bitfield);
      return trie_insert(root->children[ind], key + 1, value);
  }
  // Letter *key isn't in the tree, must add it
  int children = count_children(root->bitfield);
  trie_t *new_tree = trie_new();
  root->bitfield |= 1 << pos; // make sure the added tree appears in the children
  int position_to_add = get_index_in_children(pos, root->bitfield);

  if (!root->children) {
     root->children = calloc(1, sizeof(trie_t *));
     root->children[0] = new_tree;
      return trie_insert(new_tree, key + 1, value);
  }

  // Allocating a new array and copying the right positions
  // possibly beter to do with realloc and memmove but who has time for that in an exam
  root->children = realloc(root->children, (children + 1) * sizeof(trie_t *));
  memmove(root->children + position_to_add + 1, root->children + position_to_add, sizeof(trie_t *) * (children - position_to_add));
  root->children[position_to_add] = new_tree;

  // for (int i = 0; i < position_to_add; i++) {
  //   new_children[i] = root->children[i];
  // }
  // new_children[position_to_add] = new_tree;
  // for (int i = position_to_add; i < children; i++) {
  //   new_children[i + 1] = root->children[i];
  // }

  // free(root->children);
  // root->children = new_children;
  return trie_insert(new_tree, key + 1, value);
}

//#ifdef TRIE_MAIN

// TODO: DO NOT MODIFY TIHS FUNCTION!!! Remember to run:
//  valgrind --leak-check=full --show-leak-kinds=all ./trie

int main(void) {
  #define SIZE (8)
  char ks[SIZE][10] = { "GOOD", "",   "W",  "-_ _-", "123", "*",   "()",  "+}{" };
  bool bs[SIZE] =     { true,   true, true, true,    false, false, false, false };
  int vs[SIZE] =      { 12,     0,    -1,   2342,    999,   0,     2,     4 };

  printf("Mapping strings to ints.\n");
  trie_t *root = trie_new();
  for (int i = 0; i < SIZE; ++i) {
    char *status = bs[i] ? "(insert should succeed)" : "(insert should fail)";
    if (trie_insert(root, ks[i], vs[i])) {
      printf("Was able to insert %s with value %d %s\n", ks[i], vs[i], status);
    } else {
      printf("Unable to insert %s with value %d %s\n", ks[i], vs[i], status);
    }
  }
  for (int i = SIZE - 1; i >= 0; --i) {
    char *status = bs[i] ? "(get should succeed)" : "(get should fail)";
    int v;
    if (trie_get(root, ks[i], &v)) {
      printf("Was able to get %s -> %d %s\n", ks[i], v, status);
    } else {
      printf("Unable to get %s %s\n", ks[i], status);
    }
  }
  trie_free(root);
}

