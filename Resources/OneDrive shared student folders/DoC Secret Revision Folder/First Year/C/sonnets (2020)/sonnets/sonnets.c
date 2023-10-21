#include "sonnets.h"
#include "maps.h"

#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>

// Checks that files were opened correctly.
#define FILECHECK(ptr, msg) if (!(ptr)) perror(msg), exit(EXIT_FAILURE)
#define ASSERT_PTR_NOT_NULL(ptr, msg) if (!(ptr)) perror(msg), exit(EXIT_FAILURE)

// Returns a pointer to the last word in line, or NULL if there are no words.
// This function adds a sentinel '\0' after the word.
char *lastwordtok(char *line) {
  assert(line);
  int pos = -1;
  for (int i = 0; line[i];  ++i) if (isalpha(line[i])) pos = i;
  if (pos == -1) return NULL;
  line[pos + 1] = '\0';
  while(pos >= 0 && line[--pos] != ' ');
  while(!isalpha(line[++pos]));
  return line + pos;
}

char *uppercase(char *str) {
  assert(str);
  char *s = str;
  for (; *s; s++) {
    if (*s >= 'a' && *s <= 'z') {
        *s += 'A' - 'a';
    }
  }
  return str;
}

#define VOWELS "AEIOU"

const char *strrhyme(const char *phonemes) {
  char *s = phonemes;
  int last = 0;
  for (int i = 0; i < strlen(s); i++) {
    if (strchr(VOWELS, toupper(s[i])) && i != 0 && s[i-1] == ' ') {
        last = i;
    }
  }
  return phonemes + last;
}

char *str_clone(char *src) {
    if (!src) {
        return calloc(1, sizeof(char));
    }
    int len = strlen(src);
    char *cpy = calloc(len + 1, sizeof(char));
    for (int i = 0; i < len; i++) {
        cpy[i] = src[i];
    }
    return cpy;
}

#define MAX_DICT_LINE_LENGTH 120

// Assume the given file is formatted correctly and formed only of characters
// that are supported by the alphabet from PART A.
dict_t *load_rhyme_mappings_from_file(const char *phonetic_dict_filename) {
  FILE *file = fopen(phonetic_dict_filename, "r");
  FILECHECK(file, "Could not open file dictionary");
  char buf[MAX_DICT_LINE_LENGTH];
  dict_t *dict = dict_new();
  dict_t *rhymes = dict_new();
  int rhyme_cnt = 0;
  while (fgets(buf, MAX_DICT_LINE_LENGTH, file)) {
      char *word;
      char *stack_rhyme;
      char *rest;
      int buf_len = strlen(buf);
      for (int i = 0; i < buf_len; i++) {
        if (buf[i] == ' ') {
            buf[i] = '\0';
            word = buf;
            rest = buf + i + 1;
            break;
        }
      }
      stack_rhyme = strrhyme(rest);
      stack_rhyme[strlen(stack_rhyme) - 1] = '\0'; // delete the \n
      // printf("%s -> %s\n", word, stack_rhyme);
      int val;
      if (strintmap_get(rhymes, stack_rhyme, &val)) {
        // rhyme is in the map
        strintmap_insert(dict, word, val);
      } else {
        // rhyme isn't in the map
        strintmap_insert(rhymes, stack_rhyme, rhyme_cnt);
        strintmap_insert(dict, word, rhyme_cnt);
        rhyme_cnt++;
      }
  }
  strintmap_free(rhymes);
  fclose(file);
  return dict;
}

#define MAX_SONNET_LINE_LENGTH 80

bool next_rhyme_scheme(FILE *sonnets_file,
                       const dict_t *rhyme_mappings, char *out) {
  FILECHECK(sonnets_file, "Invalid sonnets_file");
  intcharmap_t *letters = intcharmap_new(); 
  
  // Starts with letter A and then go from there
  int out_index = 0;
  char letter = 'A';
  char buf[MAX_SONNET_LINE_LENGTH];
  bool found = false;
  while (fgets(buf, MAX_SONNET_LINE_LENGTH, sonnets_file)) {
      char *last_word = lastwordtok(buf);
      bool is_empty_line = last_word == NULL;
      if (is_empty_line && found) {
          // Reached the end of the sonnet
          break;
      }
      if (is_empty_line && !found) {
        // haven't found a sonnet yet, going on ...
        continue;
      }
      // must not be an empty line, found a sonnet or continuing the sonnet
      found = true;
      int rhyme_id;
      uppercase(last_word);
      bool valid_key = strintmap_get(rhyme_mappings, last_word, &rhyme_id);
      if (!valid_key) {
         // fprintf(stderr, "Could not find the rhyme for the word %s in the dict", last_word);
         continue;
      }
      char rhyme_letter;
      bool rhyme_id_in = intcharmap_get(letters, rhyme_id, &rhyme_letter);
      if (!rhyme_id_in) {
        intcharmap_insert(letters, rhyme_id, letter);
        out[out_index] = letter;
        out_index++;
        letter++;
        continue;
      }
      out[out_index] = rhyme_letter;
      out_index++;
  }
  intcharmap_free(letters);
  if (!found) {
    return false;
  }
  return true;
}

#define MAX_NUM_SONNET_LINES 20

void most_common_rhyme_scheme(FILE *sonnets_file,
                              const dict_t *rhyme_mappings, char *out) {
    FILECHECK(sonnets_file, "NULL sonnets_file");
    char rhyme_scheme[MAX_NUM_SONNET_LINES];
    int most_freq = 0;
    char *most_freq_scheme = NULL;
    dict_t *frequency_map = strintmap_new();
    while (next_rhyme_scheme(sonnets_file, rhyme_mappings, rhyme_scheme)) {
        int val;
        bool in_freq_map = strintmap_get(frequency_map, rhyme_scheme, &val);
        if (in_freq_map) {
            strintmap_insert(frequency_map, rhyme_scheme, val + 1);
            if (val + 1 >= most_freq) {
                most_freq = val + 1;
                if (most_freq_scheme) {
                    free(most_freq_scheme);
                }
                most_freq_scheme = str_clone(rhyme_scheme);
            }
        }
        else {
            strintmap_insert(frequency_map, rhyme_scheme, 1);

            // It's the very first scheme
            if (most_freq == 0) {
                most_freq = 1;
                most_freq_scheme = str_clone(rhyme_scheme);
            }
        }
    }
    if (!most_freq_scheme) {
        strcpy(out, "N/A");
        strintmap_free(frequency_map);
        return;
    }
    int len = strlen(most_freq_scheme);
    int exp;
    int found;
    for (int i = 0; i <= len; i++) {
        out[i] = most_freq_scheme[i]; 
    }
    free(most_freq_scheme);
    strintmap_free(frequency_map);
}


#ifdef SONNETS_MAIN

#define PHONETIC_DICT_FILE "dictionary.txt"

/* TODO: DO NOT MODIFY THIS FUNCTION!!! Remember to run:
 *  valgrind --leak-check=full --show-leak-kinds=all ./sonnets_map or
 *  valgrind --leak-check=full --show-leak-kinds=all ./sonnets_trie if your
 *  PART A is correct and want to see how much faster it is
 */
int main (void) {
  dict_t *rhyme_mappings = load_rhyme_mappings_from_file(PHONETIC_DICT_FILE);

  char *sonnets_files[3] = {"shakespeare.txt", "spenser.txt", "petrarch.txt"};
  for (int i = 0; i < 3; ++i) {
    FILE *f = fopen(sonnets_files[i], "r");
    FILECHECK(f, sonnets_files[i]);

    char rhyme_scheme[MAX_NUM_SONNET_LINES];
    most_common_rhyme_scheme(f, rhyme_mappings, rhyme_scheme);
    printf("The most common rhyme scheme of sonnets from %s is: %s\n",
           sonnets_files[i], rhyme_scheme);
    fclose(f);
  }

  dict_free(rhyme_mappings);
  return EXIT_SUCCESS;
}

#endif
