#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include "sha.h"

void readSHAInput(char* filename, SHAData* data) {
  FILE* file = fopen(filename, "r");

  // Gets the size of the file in bytes
  fseek(file, 0L, SEEK_END);
  int size = ftell(file);
  rewind(file);

  char* input = malloc(size);
  int c;
  int n = 0;
  while ((c = fgetc(file)) != EOF) {
    input[n++] = (char) c;
  }

  free(file);

  data->inputLength = size;
  data->input = input;
}

int main(int argc, char** argv) {
  if (argc < 3) {
    fprintf(stderr, "Usage: sha <input file> <output file>\n");
    exit(-1);
  }

  char* inputFileName = argv[1];
  char* outputFileName = argv[2];
  
  SHAData data;
  readSHAInput(inputFileName, &data);
  
}