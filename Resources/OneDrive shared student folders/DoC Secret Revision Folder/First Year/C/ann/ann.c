#include "ann.h"

/* Creates and returns a new ann. */
ann_t *ann_create(int num_layers, int *layer_outputs)
{
  ann_t *ann = (ann_t *) malloc(sizeof(ann_t));
  if (!ann) {
    perror("Memory allocation error");
    return NULL;
  }

  ann -> input_layer = layer_create();
  layer_init(ann -> input_layer, layer_outputs[0], NULL);
  layer_t *prev = ann -> input_layer;

  for (int i = 1; i < num_layers; i++) {
    layer_t *curr = layer_create();
    if ((!curr) || (layer_iniRnextt(curr, layer_outputs[i], prev))) {
      perror("Memory allocation error");
      return NULL;
    }
    prev = curr;
  }

  ann -> output_layer = prev;
  return ann;
}

/* Frees the space allocated to ann. */
void ann_free(ann_t *ann)
{
  layer_t *curr = ann -> input_layer;
  while (curr != ann -> output_layer) {
    layer_t *prev = curr;
    curr = curr -> next;
    layer_free(prev);
  }
  layer_free(ann -> output_layer);
  free(ann);
}

/* Forward run of given ann with inputs. */
void ann_predict(ann_t const *ann, double const *inputs)
{
  for (int i = 0; i < ann -> input_layer -> num_outputs; i++) {
    ann -> input_layer -> outputs[i] = inputs[i];
  }

  layer_t *curr = ann -> input_layer;
  while (curr != ann -> output_layer) {
    layer_compute_outputs(curr -> next);
    curr = curr -> next;
  }
}

/* Trains the ann with single backprop update. */
void ann_train(ann_t const *ann, double const *inputs, double const *targets, double l_rate)
{
  /* Sanity checks. */
  assert(ann != NULL);
  assert(inputs != NULL);
  assert(targets != NULL);
  assert(l_rate > 0);

  /* Run forward pass. */
  ann_predict(ann, inputs);

  layer_t *out = ann -> output_layer;
  for (int i = 0; i < out -> num_outputs; i++) {
    out -> deltas[i] = sigmoidprime(out -> outputs[i]) * (targets[i] - out -> outputs[i]);
  }

  layer_t *curr = out -> prev;
  while (curr != ann -> input_layer) {
    layer_compute_deltas(curr);
    curr = curr -> prev;
  }
  while (curr != ann -> output_layer) {
    curr = curr -> next;
    layer_update(curr, l_rate);
  }
}
