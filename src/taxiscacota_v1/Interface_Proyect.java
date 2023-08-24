/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxiscacota_v1;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Gustavo, Glendy, Daniel, Bustos y Angel (El profesor y el pelao)
 */
public class Interface_Proyect extends javax.swing.JFrame {
//==============================================================================

    static final int t = 20;  // tamaño

    static ArrayList<Nodos_v21> vertices = new ArrayList();
    static ArrayList<Aristas_v21> aristas = new ArrayList();
    static int[][] matrizAd = new int[t][t]; // cambia tamaño
    static int[][] matrizIn = new int[t][t * (t - 1) / 2]; // posibles aristas

    int id_ub_taxi = 0;
    int ruta1 = 0;
    int ruta2 = 0;

    Nodos_v21 nodoSelec1 = null;
    Nodos_v21 nodoSelec2 = null;

    static Taxis[] taxis = new Taxis[6];
    static int indiceTaxi = 0;

    static personas[] Personas = new personas[1];
    static int indicepersonas = 0;

    static Destino[] destino = new Destino[1];
    static int indiceDestino = 0;

    public static final int ANCHO = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int ALTO = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

    int id_ub_usuario = -1;
    int id_ub_destino = -1;

    Dijkstra disktra = new Dijkstra();
    ArrayList<Integer> camino_corto = new ArrayList<Integer>();
    ArrayList<Integer> camino_corto_Destino = new ArrayList<Integer>();

//==============================================================================
    /**
     * Creates new form Interface_Proyect
     */
    public Interface_Proyect() {
        this.setPreferredSize(new Dimension((int) (ANCHO * 0.5), (int) (ALTO * 0.6)));
        initComponents();
        this.setLocationRelativeTo(null);
        jBReset.setVisible(false);
        jBtrayecto.setVisible(false);
    }

    // validaciones----------------------------------------------------------------------------------
    /*
    Esta funcion verifica si al colocar el ususario sobre un nodo, hay un taxi sobre ese nodo.
    Si hay un taxi, genera una execepcion y no deja ubicar al usuario en ese nodo.
     */
    public void val_ubicacion_usuario(Nodos_v21 ub_usuario) throws Exception {
        for (int k = 0; k < taxis.length; k++) {
            if (taxis[k].getX() == ub_usuario.getX() && taxis[k].getY() == ub_usuario.getY()) {
                throw new Exception("No se puede ubicar un usuario sobre un taxi");
            }
        }
    }

    /*
    Esta funcion verifica que al hacer clic fuera de un nodo, genere una excepcion y no deje ubicar un usuario o destino.
     */
    public void val_click(java.awt.event.MouseEvent evt) throws Exception {
        // verficar si en el click hay un nodo
        if (ecuacionDeCirculo(evt.getPoint(), Nodos_v21.d) == null) {
            throw new Exception("No se encontro un nodo");
        }
    }

    /*
    Esta funcion valida que solo haya un usuario en el programa, si hay más de un 
    usuario pidiendo un taxi, nos genera una excepcion.
     */
    public void val_cantidad_usuarios() throws Exception {
        // verficar la cantidad de usuarios 
        if (indicepersonas == 1) {
            throw new Exception("No se pueden ubicar mas usuarios");
        }
    }

    /*
    Esta funcion valida que solo haya un destino, si hay más de un 
    destino, nos genera una excepcion.
     */
    public void val_cantidad_destinos() throws Exception {
        // verficar la cantidad de usuarios 
        if (indiceDestino == 1) {
            throw new Exception("No se pueden ubicar mas destinos");
        }
    }

    /*
    Esta funcion nos valida que al momento de pedir un taxi
    debe estar un usuario seleccionado, de lo contrario nos genera una excepcion.
     */
    public void val_arreglo_personas() throws Exception {
        // verficar la cantidad de usuarios 
        if (indicepersonas == 0) {
            throw new Exception("No se ha ubicado un usuario");
        }
    }

    public Nodos_v21 ecuacionDeCirculo(Point punto, int RADIO) {
        return verificarPosicion((punto.x), (punto.y), RADIO / 2);
    }

    private Nodos_v21 verificarPosicion(int x, int y, int radMed) {
        for (Nodos_v21 nodo : vertices) {
            if (nodo != null) {
                if (x > (nodo.getX() - radMed) && x < (nodo.getX() + radMed)) {
                    if (y > (nodo.getY() - radMed) && y < (nodo.getY() + radMed)) {
                        return nodo;
                    }
                }
            }
        }
        return null;
    }

    /*
    Esta funcion nos va a almacenar en un arrayList las coordenadas xy de cada taxi.
    @return
    - posiciones: ArrayList de array de enteros con las coordenadas de ubicacion xy de cada taxi.
     */
    public ArrayList<int[]> obtener_coordendas_ubicacion_todos_taxi() {
        ArrayList<int[]> pocisiones = new ArrayList<>();
        for (int k = 0; k < taxis.length; k++) {
            int[] pos = new int[2];
            pos[0] = taxis[k].getX();
            pos[1] = taxis[k].getY();
            pocisiones.add(pos);
        }
        return pocisiones;
    }

    /*
    @param
    - ub_taxis_ ArrayList con las coordenadas XY de los taxis.
    Esta funcion verifica que las coordenadas xy del nodo sean las mismas que las del taxi,
    una vez verificado esto, se procede en un arrayList de enteros a almacenar los id de los nodos que tienen un taxi.
    @return
    - id_ub_nodos ArrayList con los id de los nodos con taxi.
     */
    public ArrayList<Integer> obtener_id_ub_nodos(ArrayList<int[]> ub_taxis) {
        ArrayList<Integer> id_ub_taxis = new ArrayList<Integer>();
        for (int k = 0; k < vertices.size(); k++) {
            for (int i = 0; i < ub_taxis.size(); i++) {
                if (vertices.get(k).getX() == ub_taxis.get(i)[0] && vertices.get(k).getY() == ub_taxis.get(i)[1]) {
                    id_ub_taxis.add(vertices.get(k).getid_ubicacion());
                }
            }
        }
        return id_ub_taxis;
    }

    /*
    Esta funcion nos genera la ruta más corta desde la ubicacion de inicio
    hasta la ubicacion de destino.
    Nos verifica si la ubicacion del ususario es menor a la ubicacion del taxi, 
    si lo es, nos busca la ruta mas corta entre el usuario y el taxi, de lo contrario
    nos busca la ruta mpas corta entre el taxi y el usuario.
    @return
    - menor: valor de la sumatoria de los nodos de la ruta más corta.
     */
    public int ruta(ArrayList<Integer> id_ub_nodos, int id_ub_usuario) {
        int menor, ub_taxi, rout, destino;
        ub_taxi = 0;
        ArrayList<Integer> todas_r = new ArrayList<Integer>();
        for (int i = 0; i < id_ub_nodos.size(); i++) {
            ub_taxi = id_ub_nodos.get(i);
            if (id_ub_usuario < ub_taxi) {
                rout = disktra.ruta_mas_corta(id_ub_usuario, ub_taxi);
            } else {
                rout = disktra.ruta_mas_corta(ub_taxi, id_ub_usuario);
            }
            todas_r.add(rout);
        }
        menor = Collections.min(todas_r);
        return menor;
    }

    /*
    @param
    - ub_nodos: ArrayList de arrays con los id de los nodos.
    - ub_usuario: id del nodo donde esta ubicado el usuario.
    - Valor de la ruta más corta desde iub_usuario hasta el destino.
    
    Esta funcion es la encargada de obtener el id de ubicacion del taxi.
    Una vez obtenido el valor de laruta mas corta se verifica si el valor
    de rout es igual al de valor_ruta_corta, si lo es id_ub_taxi lo iguala
    al valor de ub_taxi.
    
    @return
    - ub_taxi: id del nodo en el que está el taxi.
    - Si no encuentra nada, retorna -1.
     */
    public int extraer_nodo_taxi_ruta_mas_corta(ArrayList<Integer> id_ub_nodos, int ub_usuario, int valor_ruta_corta) {
        int ub_taxi, rout = 0;
        for (int k = 0; k < id_ub_nodos.size(); k++) {
            ub_taxi = id_ub_nodos.get(k);
            if (ub_usuario < ub_taxi) {
                rout = disktra.ruta_mas_corta(ub_usuario, ub_taxi);
                if (rout == valor_ruta_corta) {
                    id_ub_taxi = ub_taxi;
                    return ub_taxi;
                }
            } else {
                rout = disktra.ruta_mas_corta(ub_taxi, ub_usuario);
                if (rout == valor_ruta_corta) {
                    id_ub_taxi = ub_taxi;
                    return ub_usuario;
                }
            }
        }
        return -1;
    }

    public void obtener_trayecto_corto(int id_nodo_final) {
        camino_corto = disktra.obtener_camino_corto(disktra.rutas, id_nodo_final);
    }

    public void pintar_ruta() {
        int ant, post;
        for (int i = 0; i < camino_corto.size() - 1; i++) {
            ant = camino_corto.get(i);
            post = camino_corto.get(i + 1);
            for (int k = 0; k < aristas.size(); k++) {
                boolean nodoiniciovef = aristas.get(k).getN1().getid_ubicacion() == ant;
                boolean nodofinalvef = aristas.get(k).getN2().getid_ubicacion() == post;
                boolean rnodoiniciovef = aristas.get(k).getN1().getid_ubicacion() == post;
                boolean rnodofinalvef = aristas.get(k).getN2().getid_ubicacion() == ant;
                if ((nodoiniciovef && nodofinalvef) || (rnodoiniciovef && rnodofinalvef)) {
                    aristas.get(k).pintarAristaNaranja(jPLienzo.getGraphics()); // Graphics
                }
            }
        }
    }

    // metodos para ruta destino
    public int ruta_destino(int id_ub_nodo, int id_ub_usuario) {
        int menor, rout, destino;
        destino = 0;
        ArrayList<Integer> totalRutas = new ArrayList<Integer>();
        destino = id_ub_nodo;
        if (id_ub_usuario < destino) {
            rout = disktra.ruta_mas_corta(id_ub_usuario, destino);
        } else {
            rout = disktra.ruta_mas_corta(destino, id_ub_usuario);
        }

        totalRutas.add(rout);
        menor = Collections.min(totalRutas);
        return menor;
    }

    /*
    @param
    - id_nodo_destino: id del nodo donde está el destino seleccionado.
    Esta funcion obtiene un arrayList de enteros de la clase Disktra (Nodos por donde pasa el camino más corto) 
    y lo almacena en la variable global camino_corto_D.
     */
    public void obtener_trayecto(int id_nodo_destino) {
        camino_corto_Destino = disktra.obtener_camino_corto(disktra.rutas, id_nodo_destino);
    }

    /*
    Esta funcion es la encargada de pintar el recorrido de la ruta más corta 
    del usuario al taxi más cercano.
     */
    public void pintar_ruta_Destino() {
        int ant, post;
        for (int i = 0; i < camino_corto_Destino.size() - 1; i++) {
            ant = camino_corto_Destino.get(i);
            post = camino_corto_Destino.get(i + 1);
            for (int k = 0; k < aristas.size(); k++) {
                boolean nodoiniciovef = aristas.get(k).getN1().getid_ubicacion() == ant;
                boolean nodofinalvef = aristas.get(k).getN2().getid_ubicacion() == post;
                boolean rnodoiniciovef = aristas.get(k).getN1().getid_ubicacion() == post;
                boolean rnodofinalvef = aristas.get(k).getN2().getid_ubicacion() == ant;
                if ((nodoiniciovef && nodofinalvef) || (rnodoiniciovef && rnodofinalvef)) {
                    aristas.get(k).pintarAristaD(jPLienzo.getGraphics()); // Graphics
                }
            }
        }
    }

    /*
    @param
    - distancia_a_recorrer: Distancia entre el usuario y el taxi.
    Esta funcion nos calcula una estimación del tiempo que le tomaría al taxi
    llegar hasta el usuario, el taxi posee una velocidad media de 30km/h.
    @return
    - tiempo: Tiempo estimado de llegada.
     */
    public double cal_tiempo(int distancia_a_recorrer) {
        double tiempo = 0;
        tiempo = (distancia_a_recorrer / 30.0) * 60; // Velocidad de 30 Km/h
        return tiempo;
    }

    /*
    Esta es la funcion encargada de mostrar los valores en cada uno
    de los campos de la tabla busqueda.
     */
    public void tabla_buscar() {
        String usuario = "";
        String taxi = "";
        int tiempo = (int) (cal_tiempo(ruta1));
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getid_ubicacion() == id_ub_usuario) {
                usuario = vertices.get(i).getNombreN();
            }
            if (vertices.get(i).getid_ubicacion() == id_ub_taxi) {
                taxi = vertices.get(i).getNombreN();
            }
        }
        JTF_User.setText(usuario);
        JTF_Taxi.setText(taxi);
        JTF_Distancia1.setText(ruta1 + " Km");
        JTF_Tiempo1.setText(tiempo + " Minuto(s)");
    }

    /*
    Esta es la funcion encargada de mostrar los valores en cada uno
    de los campos de la tabla trayecto.
     */
    public void tabla_trayecto() {
        String destino = "";
        String usuario = "";
        int tiempo = (int) (cal_tiempo(ruta2));
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getid_ubicacion() == id_ub_usuario) {
                usuario = vertices.get(i).getNombreN();
            }
            if (vertices.get(i).getid_ubicacion() == id_ub_destino) {
                destino = vertices.get(i).getNombreN();
            }
        }
        JTF_Inicio.setText(usuario);
        JTF_Final.setText(destino);
        JTF_Distancia2.setText(ruta2 + " Km");
        JTF_Tiempo2.setText(tiempo + " Minuto(s)");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBbuscar1 = new javax.swing.JButton();
        jPFondoMapa = new javax.swing.JPanel();
        img = new javax.swing.JLabel();
        jPLienzo = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        JTF_Tiempo1 = new javax.swing.JTextField();
        JL_Tiempo1 = new javax.swing.JLabel();
        JTF_Distancia1 = new javax.swing.JTextField();
        JL_Distancia1 = new javax.swing.JLabel();
        JL_NodoTaxi = new javax.swing.JLabel();
        JTF_User = new javax.swing.JTextField();
        JL_NodoUsuario = new javax.swing.JLabel();
        JTF_Taxi = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        JL_NodoInicial = new javax.swing.JLabel();
        JL_NodoFinal = new javax.swing.JLabel();
        JL_Distancia2 = new javax.swing.JLabel();
        Jl_Tiempo2 = new javax.swing.JLabel();
        JTF_Inicio = new javax.swing.JTextField();
        JTF_Final = new javax.swing.JTextField();
        JTF_Distancia2 = new javax.swing.JTextField();
        JTF_Tiempo2 = new javax.swing.JTextField();
        JBcerrar = new javax.swing.JButton();
        jBIniciar = new javax.swing.JButton();
        jBReset = new javax.swing.JButton();
        jBbuscar = new javax.swing.JButton();
        jBtrayecto = new javax.swing.JButton();

        jBbuscar1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jBbuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iniciar/taxi_72.png"))); // NOI18N
        jBbuscar1.setText("INICIAR");
        jBbuscar1.setToolTipText("");
        jBbuscar1.setContentAreaFilled(false);
        jBbuscar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBbuscar1.setName("jBIniciar"); // NOI18N
        jBbuscar1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iniciar/taxi_white_72_peque.png"))); // NOI18N
        jBbuscar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBbuscar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBbuscar1MouseClicked(evt);
            }
        });
        jBbuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBbuscar1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPFondoMapa.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPFondoMapa.setName("jPanel_Mapa"); // NOI18N
        jPFondoMapa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondo.jpg"))); // NOI18N
        img.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgMouseClicked(evt);
            }
        });
        jPFondoMapa.add(img, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 580));

        jPLienzo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Grafo"));
        jPLienzo.setPreferredSize(new java.awt.Dimension(365, 365));
        jPLienzo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPLienzoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPLienzoLayout = new javax.swing.GroupLayout(jPLienzo);
        jPLienzo.setLayout(jPLienzoLayout);
        jPLienzoLayout.setHorizontalGroup(
            jPLienzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
        );
        jPLienzoLayout.setVerticalGroup(
            jPLienzoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
        );

        jPFondoMapa.add(jPLienzo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, 580));

        getContentPane().add(jPFondoMapa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 610, 580));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setName("jPanel_Tabla"); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 204, 255)), "BÚSQUEDA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 12))); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JTF_Tiempo1.setEditable(false);
        JTF_Tiempo1.setBackground(new java.awt.Color(229, 239, 249));
        jPanel4.add(JTF_Tiempo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 150, -1));

        JL_Tiempo1.setText("Tiempo:");
        jPanel4.add(JL_Tiempo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 50, 20));

        JTF_Distancia1.setEditable(false);
        JTF_Distancia1.setBackground(new java.awt.Color(229, 239, 249));
        jPanel4.add(JTF_Distancia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 150, -1));

        JL_Distancia1.setText("Distancia:");
        jPanel4.add(JL_Distancia1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 60, 20));

        JL_NodoTaxi.setText("Nodo Taxi:");
        jPanel4.add(JL_NodoTaxi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 60, 20));

        JTF_User.setEditable(false);
        JTF_User.setBackground(new java.awt.Color(234, 241, 249));
        jPanel4.add(JTF_User, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 150, -1));

        JL_NodoUsuario.setText("Nodo Usuario:");
        jPanel4.add(JL_NodoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        JTF_Taxi.setEditable(false);
        JTF_Taxi.setBackground(new java.awt.Color(229, 239, 249));
        jPanel4.add(JTF_Taxi, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 150, -1));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 180));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 204, 255)), "TRAYECTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 12))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JL_NodoInicial.setText("Nodo Inicial:");
        jPanel1.add(JL_NodoInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 80, 20));

        JL_NodoFinal.setText("Nodo Final:");
        jPanel1.add(JL_NodoFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, 20));

        JL_Distancia2.setText("Distancia:");
        jPanel1.add(JL_Distancia2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 60, 20));

        Jl_Tiempo2.setText("Tiempo:");
        jPanel1.add(Jl_Tiempo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 60, 20));

        JTF_Inicio.setEditable(false);
        JTF_Inicio.setBackground(new java.awt.Color(229, 239, 249));
        jPanel1.add(JTF_Inicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 150, -1));

        JTF_Final.setEditable(false);
        JTF_Final.setBackground(new java.awt.Color(229, 239, 249));
        jPanel1.add(JTF_Final, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 150, -1));

        JTF_Distancia2.setEditable(false);
        JTF_Distancia2.setBackground(new java.awt.Color(229, 239, 249));
        jPanel1.add(JTF_Distancia2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 150, -1));

        JTF_Tiempo2.setEditable(false);
        JTF_Tiempo2.setBackground(new java.awt.Color(229, 239, 249));
        jPanel1.add(JTF_Tiempo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 150, -1));

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 250, 180));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 270, 380));

        JBcerrar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JBcerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerrar-simbolo-de-boton-circular (1).png"))); // NOI18N
        JBcerrar.setText("CERRAR");
        JBcerrar.setToolTipText("");
        JBcerrar.setContentAreaFilled(false);
        JBcerrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        JBcerrar.setName("jBIniciar"); // NOI18N
        JBcerrar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerrar-simbolo-de-boton-circular (1).png"))); // NOI18N
        JBcerrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        JBcerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBcerrarMouseClicked(evt);
            }
        });
        JBcerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBcerrarActionPerformed(evt);
            }
        });
        getContentPane().add(JBcerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 400, 100, 110));

        jBIniciar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jBIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iniciar/taxi_72.png"))); // NOI18N
        jBIniciar.setText("INICIAR");
        jBIniciar.setToolTipText("");
        jBIniciar.setContentAreaFilled(false);
        jBIniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBIniciar.setName("jBIniciar"); // NOI18N
        jBIniciar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/iniciar/taxi_white_72_peque.png"))); // NOI18N
        jBIniciar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBIniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBIniciarMouseClicked(evt);
            }
        });
        jBIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBIniciarActionPerformed(evt);
            }
        });
        getContentPane().add(jBIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 400, 100, 110));

        jBReset.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jBReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reset/reset_black_72.png"))); // NOI18N
        jBReset.setText("RESET");
        jBReset.setContentAreaFilled(false);
        jBReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBReset.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reset/reset_black_72_peque.png"))); // NOI18N
        jBReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBResetMouseClicked(evt);
            }
        });
        jBReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBResetActionPerformed(evt);
            }
        });
        getContentPane().add(jBReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 400, 100, 110));

        jBbuscar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jBbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ubicacion-de-busqueda (1) (1).png"))); // NOI18N
        jBbuscar.setText("BUSCAR");
        jBbuscar.setToolTipText("");
        jBbuscar.setContentAreaFilled(false);
        jBbuscar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBbuscar.setName("jBIniciar"); // NOI18N
        jBbuscar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ubicacion-de-busqueda (1) (1).png"))); // NOI18N
        jBbuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBbuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBbuscarMouseClicked(evt);
            }
        });
        jBbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBbuscarActionPerformed(evt);
            }
        });
        getContentPane().add(jBbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 520, 110, 110));

        jBtrayecto.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jBtrayecto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trayecto.png"))); // NOI18N
        jBtrayecto.setText("TRAYECTO");
        jBtrayecto.setToolTipText("");
        jBtrayecto.setContentAreaFilled(false);
        jBtrayecto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtrayecto.setName("jBIniciar"); // NOI18N
        jBtrayecto.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trayecto.png"))); // NOI18N
        jBtrayecto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtrayecto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jBtrayectoMouseClicked(evt);
            }
        });
        jBtrayecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtrayectoActionPerformed(evt);
            }
        });
        getContentPane().add(jBtrayecto, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 520, 110, 110));

        pack();
    }// </editor-fold>//GEN-END:initComponents
//==============================================================================
    private void jPLienzoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPLienzoMouseClicked

    }//GEN-LAST:event_jPLienzoMouseClicked

    private void imgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgMouseClicked
        int x = evt.getX();
        int y = evt.getY();
        Nodos_v21 auxNodo;
        auxNodo = ecuacionDeCirculo(evt.getPoint(), Nodos_v21.d);
        if (SwingUtilities.isLeftMouseButton(evt)) {
            try {
                val_cantidad_usuarios();
                val_click(evt);
                val_ubicacion_usuario(auxNodo);
                id_ub_usuario = auxNodo.getid_ubicacion();
                personas persona = new personas(x, y, id_ub_usuario);
                Personas[indicepersonas] = (persona);
                indicepersonas++;
                persona.pintarpersonas(jPLienzo.getGraphics());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

        if (SwingUtilities.isRightMouseButton(evt)) {
            try {
                val_click(evt);
                val_cantidad_destinos();
                val_arreglo_personas();
                id_ub_destino = auxNodo.getid_ubicacion();
                Destino d = new Destino(x, y, id_ub_destino);
                destino[indiceDestino] = (d);
                indiceDestino++;
                d.pintarDestino(jPLienzo.getGraphics());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }//GEN-LAST:event_imgMouseClicked

    private void JBcerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBcerrarMouseClicked
        System.exit(0);
    }//GEN-LAST:event_JBcerrarMouseClicked

    private void JBcerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBcerrarActionPerformed

    }//GEN-LAST:event_JBcerrarActionPerformed

    private void jBIniciarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBIniciarMouseClicked
        Nodos_v21 n = new Nodos_v21(81, 31, "Terpel 3");
        vertices.add(n);
        n = new Nodos_v21(153, 102, "Colegio Municipal");
        vertices.add(n);
        n = new Nodos_v21(247, 165, "Tienda Sofía");
        vertices.add(n);
        n = new Nodos_v21(304, 116, "Batallón");
        vertices.add(n);
        n = new Nodos_v21(361, 181, "Cotranal");
        vertices.add(n);
        n = new Nodos_v21(432, 133, "La Y");
        vertices.add(n);
        n = new Nodos_v21(512, 19, "Terpel 1");
        vertices.add(n);
        n = new Nodos_v21(267, 314, "Fresicrema");
        vertices.add(n);
        n = new Nodos_v21(335, 281, "Policía");
        vertices.add(n);
        n = new Nodos_v21(398, 247, "Bomberos");
        vertices.add(n);
        n = new Nodos_v21(465, 207, "Central Taxis");
        vertices.add(n);
        n = new Nodos_v21(153, 483, "Terpel 2");
        vertices.add(n);
        n = new Nodos_v21(304, 376, "MASxMENOS");
        vertices.add(n);
        n = new Nodos_v21(372, 340, "Iglesia 1");
        vertices.add(n);
        n = new Nodos_v21(431, 309, "Don Pancho");
        vertices.add(n);
        n = new Nodos_v21(503, 273, "Taller de Motos");
        vertices.add(n);
        n = new Nodos_v21(334, 451, "El Descanso");
        vertices.add(n);
        n = new Nodos_v21(386, 425, "Droguería");
        vertices.add(n);
        n = new Nodos_v21(468, 382, "Clínica");
        vertices.add(n);
        n = new Nodos_v21(535, 344, "Terminal");
        vertices.add(n);
        n = new Nodos_v21(362, 511, "DIAN");
        vertices.add(n);
        n = new Nodos_v21(402, 503, "Fiscalia");
        vertices.add(n);
        n = new Nodos_v21(502, 453, "D1");
        vertices.add(n);
        n = new Nodos_v21(559, 412, "ARA");
        vertices.add(n);
        n = new Nodos_v21(420, 556, "Drogueria 2");
        vertices.add(n);
        n = new Nodos_v21(553, 546, "Isimo");
        vertices.add(n);
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).pintarNodo(jPLienzo.getGraphics());
        }

        Aristas_v21 arista = new Aristas_v21(vertices.get(0), vertices.get(1), 7);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(1), vertices.get(2), 7);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(1), vertices.get(7), 9);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(2), vertices.get(3), 3);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(2), vertices.get(8), 8);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(3), vertices.get(4), 5);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(4), vertices.get(5), 6);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(4), vertices.get(9), 6);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(5), vertices.get(6), 5);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(5), vertices.get(10), 6);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(7), vertices.get(8), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(7), vertices.get(12), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(8), vertices.get(9), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(8), vertices.get(13), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(9), vertices.get(10), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(9), vertices.get(14), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(10), vertices.get(15), 6);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(11), vertices.get(12), 8);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(12), vertices.get(13), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(12), vertices.get(16), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(13), vertices.get(14), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(13), vertices.get(17), 6);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(14), vertices.get(15), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(14), vertices.get(18), 5);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(15), vertices.get(19), 5);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(16), vertices.get(17), 3);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(16), vertices.get(20), 3);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(17), vertices.get(18), 6);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(17), vertices.get(21), 5);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(18), vertices.get(19), 5);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(18), vertices.get(22), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(19), vertices.get(23), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(20), vertices.get(21), 2);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(21), vertices.get(22), 7);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(21), vertices.get(24), 3);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(22), vertices.get(23), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics
        arista = new Aristas_v21(vertices.get(22), vertices.get(25), 4);
        aristas.add(arista);
        arista.pintarArista(jPLienzo.getGraphics()); // Graphics

        // pintar taxis
        int x = evt.getX();
        int y = evt.getY();
        int t = vertices.size() - 1;
        Random num = new Random();
        int nn = 0;

        ArrayList<Integer> pos = new ArrayList();
        for (int i = 0; i < taxis.length; i++) {
            nn = num.nextInt(t - 1) + 0;
            if (pos.contains(nn) == false) {
                x = vertices.get(nn).getX();
                y = vertices.get(nn).getY();
                Taxis taxi = new Taxis(x, y);
                taxis[indiceTaxi] = (taxi);
                indiceTaxi++;
                taxi.pintarTaxi(jPLienzo.getGraphics());
                pos.add(nn);
            } else {
                i--;
            }
        }

        // efecto botones
        jBIniciar.setVisible(false);
        jBReset.setVisible(true);
    }//GEN-LAST:event_jBIniciarMouseClicked

    private void jBIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBIniciarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBIniciarActionPerformed

    private void jBResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBResetMouseClicked
        // codigo cuando se haga click 
        // borrar el lienzo
        jPLienzo.removeAll();
        jPLienzo.repaint();
        // inicializar los arreglos
        vertices = new ArrayList();
        aristas = new ArrayList();
        taxis = new Taxis[6];
        indiceTaxi = 0;

        Personas = new personas[1];
        indicepersonas = 0;

        destino = new Destino[1];
        indiceDestino = 0;
        // cambios en los botones
        jBReset.setVisible(false);
        jBIniciar.setVisible(true);

        jBbuscar.setVisible(true);
        jBtrayecto.setVisible(false);

        JTF_User.setText(null);
        JTF_Taxi.setText(null);
        JTF_Distancia1.setText(null);
        JTF_Tiempo1.setText(null);
        JTF_Inicio.setText(null);
        JTF_Final.setText(null);
        JTF_Distancia2.setText(null);
        JTF_Tiempo2.setText(null);

        id_ub_usuario = -1;
        id_ub_destino = -1;
    }//GEN-LAST:event_jBResetMouseClicked

    private void jBResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBResetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBResetActionPerformed

    private void jBbuscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBbuscarMouseClicked

        if (id_ub_usuario == -1) {
            JOptionPane.showMessageDialog(null, "No ha ubicado el usuario");
        } else {
            ArrayList<Integer> id_nodos_posiciones_taxi = new ArrayList<Integer>();
            ArrayList<int[]> pocisiones_taxis = new ArrayList<>();
            pocisiones_taxis = obtener_coordendas_ubicacion_todos_taxi();
            id_nodos_posiciones_taxi = obtener_id_ub_nodos(pocisiones_taxis);
            int valor_ruta_corta = ruta(id_nodos_posiciones_taxi, id_ub_usuario);
            int nodo_final_valor_ruta_corta = extraer_nodo_taxi_ruta_mas_corta(id_nodos_posiciones_taxi, id_ub_usuario, valor_ruta_corta);
            obtener_trayecto_corto(nodo_final_valor_ruta_corta);
            pintar_ruta();
            jBbuscar.setVisible(false);
            jBtrayecto.setVisible(true);
            ruta1 = valor_ruta_corta;
            tabla_buscar();
        }
    }//GEN-LAST:event_jBbuscarMouseClicked

    private void jBbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBbuscarActionPerformed

    private void jBbuscar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBbuscar1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jBbuscar1MouseClicked

    private void jBbuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBbuscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBbuscar1ActionPerformed

    private void jBtrayectoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBtrayectoMouseClicked

        if (id_ub_usuario == -1 || id_ub_destino == -1) {
            JOptionPane.showMessageDialog(null, "No ha ubicado el usuario y/o el destino");
        } else {
            int valor_ruta_corta = ruta_destino(id_ub_destino, id_ub_usuario);
            if (id_ub_usuario < id_ub_destino) {
                obtener_trayecto(id_ub_destino);
            } else {
                obtener_trayecto(id_ub_usuario);
            }
            pintar_ruta_Destino();

            jBbuscar.setVisible(true);
            jBtrayecto.setVisible(false);
            ruta2 = valor_ruta_corta;
            tabla_trayecto();
        }
    }//GEN-LAST:event_jBtrayectoMouseClicked

    private void jBtrayectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtrayectoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtrayectoActionPerformed
//==============================================================================

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface_Proyect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface_Proyect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface_Proyect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface_Proyect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface_Proyect().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBcerrar;
    private javax.swing.JLabel JL_Distancia1;
    private javax.swing.JLabel JL_Distancia2;
    private javax.swing.JLabel JL_NodoFinal;
    private javax.swing.JLabel JL_NodoInicial;
    private javax.swing.JLabel JL_NodoTaxi;
    private javax.swing.JLabel JL_NodoUsuario;
    private javax.swing.JLabel JL_Tiempo1;
    private javax.swing.JTextField JTF_Distancia1;
    private javax.swing.JTextField JTF_Distancia2;
    private javax.swing.JTextField JTF_Final;
    private javax.swing.JTextField JTF_Inicio;
    private javax.swing.JTextField JTF_Taxi;
    private javax.swing.JTextField JTF_Tiempo1;
    private javax.swing.JTextField JTF_Tiempo2;
    private javax.swing.JTextField JTF_User;
    private javax.swing.JLabel Jl_Tiempo2;
    private javax.swing.JLabel img;
    private javax.swing.JButton jBIniciar;
    private javax.swing.JButton jBReset;
    private javax.swing.JButton jBbuscar;
    private javax.swing.JButton jBbuscar1;
    private javax.swing.JButton jBtrayecto;
    private javax.swing.JPanel jPFondoMapa;
    private javax.swing.JPanel jPLienzo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
