/****
 *
 * Arquivo: ppmccod.c
 *
 * Autor: Gutenberg Pessoa Botelho Neto
 *
 * Data de Criação: 13/07/2009
 * Última modificação: 13/07/2009
 *
 * Descrição: Programa para codificar um arquivo utilizando o modelo PPM-C
 *
 ****/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char** argv) {


    if ((argc < 2) || (argc > 4)) { // testar valor passado como contexto
        printf ("Erro na chamada do programa\n");
        printf ("Formas de utilizacao:\n");
        printf ("\tppmccod nomearqentrada\n");
        printf ("\tppmccod nomearqentrada nomearqsaida\n");
        printf ("\tppmccod nomearqentrada nomearqsaida contexto[0-3]\n");
        return (EXIT_FAILURE);
    }

    return (EXIT_SUCCESS);
}

