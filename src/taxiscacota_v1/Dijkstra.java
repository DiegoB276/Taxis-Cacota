package taxiscacota_v1;

import java.util.*;

/**
 *
 * @author https://www.youtube.com/watch?v=U8U8E0QYCUc post creditos : proyecto
 * taxis cacota.
 */
public class Dijkstra {

    public Dijkstra() {
    }

    //Arreglo de enteros de las distancias del punto de partida con los demas nodos.
    public int distance[] = new int[30];

    //Matriz que almacena todos los pesos
    public int cost[][] = new int[30][30];

    // ArrayList para almacenar las rutas más cortas desde el nodo fuente hasta cada nodo visitado.
    public ArrayList<ArrayList<Integer>> shortestPaths = new ArrayList<>();

    //ArrayList de Arrays de las rutas de los nodos por deonde pasa la ruta.
    public ArrayList<ArrayList<Integer>> rutas = new ArrayList<>();

    /*
    @Params{*}
     - n: Numero de nodos.
     - s: Nodo de Inicio posicion usuario.
     */
    public void calc(int numNodos, int nodoUsuario) {
        int flag[] = new int[numNodos + 1];
        int prevNode[] = new int[numNodos + 1]; // Nuevo arreglo para almacenar los nodos previos en la ruta más corta.
        int i, minpos = 1, k, c, minimum;

        for (i = 1; i <= numNodos; i++) {
            flag[i] = 0;
            this.distance[i] = this.cost[nodoUsuario][i];
            prevNode[i] = nodoUsuario; // Inicialmente, asumimos que la ruta más corta es directamente desde el nodo fuente 's'.
        }
        c = 2;
        while (c <= numNodos) {
            minimum = 99;
            for (k = 1; k <= numNodos; k++) {
                if (this.distance[k] < minimum && flag[k] != 1) {
                    minimum = this.distance[k];
                    minpos = k;
                }
            }
            flag[minpos] = 1;
            c++;
            for (k = 1; k <= numNodos; k++) {
                if (this.distance[minpos] + this.cost[minpos][k] < this.distance[k] && flag[k] != 1) {
                    this.distance[k] = this.distance[minpos] + this.cost[minpos][k];
                    prevNode[k] = minpos;
                }
            }
        }

        for (i = 1; i <= numNodos; i++) {
            if (i != nodoUsuario) {
                ArrayList<Integer> path = new ArrayList<>();
                path.add(i); // Agregamos el nodo de destino al principio de la lista.
                printPath(prevNode, i, nodoUsuario, path); // Llamamos a una función auxiliar para obtener los nodos del camino.
                shortestPaths.add(path);
            }
        }
    }

    // Función auxiliar para imprimir la ruta más corta desde el nodo fuente hasta un nodo dado.
    private void printPath(int[] prevNode, int currentNode, int s, ArrayList<Integer> path) {
        if (currentNode == s) {
            path.add(s);
        } else if (prevNode[currentNode] == s) {
            path.add(s);
            path.add(currentNode);
        } else {
            printPath(prevNode, prevNode[currentNode], s, path);
            path.add(currentNode);
        }
    }

    /*
    Funcion encargada de convertir un string largo de datos a una matriz.
    @params
    - input: String con todos los datos separados por espacios.
    
    @return
    - marix: Matriz de enteros con todos los datos.
     */
    public static int[][] convertStringToMatrix(String input) {
        String[] elements = input.split(" ");
        int[][] matrix = new int[26][26];

        int index = 0;
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                if (index < elements.length) {
                    matrix[i][j] = Integer.parseInt(elements[index]);
                    index++;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
        return matrix;
    }

    /*
    Obtiene el camino más corto.
    @params
    - caminos: ArrayList de Integers de todos los nodos por donde pasan las rutas rutas.caArrayList auxiliar el cual almacenaráNOm
    - nodo_final: Posicion del taxi.
    
    Esta funcion extrae un arrayList de caminos, removemos el primer elemento que es la copia del ultimo.
    @return
    - Retorna la ruta más corta hasta la ruta final.
     */
    public ArrayList<Integer> obtener_camino_corto(ArrayList<ArrayList<Integer>> caminos, int nodo_final) {
        ArrayList<Integer> ruta = new ArrayList<Integer>();
        ruta = caminos.get(nodo_final - 2);
        ruta.remove(0);
        return ruta;
    }

    /*
    Funcion para hallar la ruta más corta,
    @params
    - inicio: posicion inicial del usuario (nodo).
    - end: posición del taxi o destino (nodo).
     */
    public int ruta_mas_corta(int inicio, int end) {
        int result;
        String m = "0 7 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 7 0 7 999 999 999 999 9 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 7 0 3 999 999 999 999 8 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 3 0 5 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 5 0 6 999 999 999 6 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 6 0 5 999 999 999 6 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 5 0 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 9 999 999 999 999 999 0 4 999 999 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 8 999 999 999 999 4 0 4 999 999 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 6 999 999 999 4 0 4 999 999 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 6 999 999 999 4 0 999 999 999 999 6 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 0 8 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 999 8 0 4 999 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 999 4 0 4 999 999 6 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 999 4 0 4 999 999 5 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 6 999 999 999 4 0 999 999 999 5 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 999 0 3 999 999 3 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 6 999 999 3 0 6 999 999 5 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 5 999 999 6 0 5 999 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 5 999 999 5 0 999 999 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 3 999 999 999 0 2 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 5 999 999 2 0 7 999 3 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 7 0 4 999 4 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 4 0 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 3 999 999 0 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 999 4 999 999 0";
        int[][] matr = new int[26][26];
        matr = convertStringToMatrix(m);
        Dijkstra dikstra = new Dijkstra();
        for (int i = 1; i <= 26; i++) {
            for (int j = 1; j <= 26; j++) {
                dikstra.cost[i][j] = matr[i - 1][j - 1];
                if (dikstra.cost[i][j] == 0) {
                    dikstra.cost[i][j] = 999;
                }
            }
        }
        dikstra.calc(26, inicio);
        rutas = dikstra.shortestPaths;
        result = dikstra.distance[end];
        return result;
    }
}
