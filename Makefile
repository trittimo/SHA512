sha: sha.c sha.h
	gcc sha.c -o3 -Werror -Wpedantic -std=c99 -o sha

debug: sha.c sha.h
	gcc sha.c -o3 -Werror -Wpedantic -std=c99 -o sha_debug -DDEBUG

clean:
	rm -rf *.o sha sha_debug