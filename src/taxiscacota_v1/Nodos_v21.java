/* UNIVERSIDAD DE PAMPLONA
    Ingeniería de Sistemas - Matemáticas discretas: Grafos
    M.Sc. Luis Armando Portilla Granados
    Actualizado octubre de 2022: ArrayList
 */
package taxiscacota_v1;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.RenderingHints;

/**
 *
 * @author Mariana
 */
public class Nodos_v21 {

    private int x;
    private int y;
    private String nombreN;
    private int id_ubicacion;
    static final int d = 18;  // diámetro para el Circulo

    public Nodos_v21(int x, int y, String nombre) {
        this.x = x;
        this.y = y;
        this.nombreN = nombre;
        if (nombre.equals("Terpel 3")) {
            this.id_ubicacion = 1;
        }
        if (nombre.equals("Colegio Municipal")) {
            this.id_ubicacion = 2;
        }
        if (nombre.equals("Tienda Sofía")) {
            this.id_ubicacion = 3;
        }
        if (nombre.equals("Batallón")) {
            this.id_ubicacion = 4;
        }
        if (nombre.equals("Cotranal")) {
            this.id_ubicacion = 5;
        }
        if (nombre.equals("La Y")) {
            this.id_ubicacion = 6;
        }
        if (nombre.equals("Terpel 1")) {
            this.id_ubicacion = 7;
        }
        if (nombre.equals("Fresicrema")) {
            this.id_ubicacion = 8;
        }
        if (nombre.equals("Policía")) {
            this.id_ubicacion = 9;
        }
        if (nombre.equals("Bomberos")) {
            this.id_ubicacion = 10;
        }
        if (nombre.equals("Central Taxis")) {
            this.id_ubicacion = 11;
        }
        if (nombre.equals("Terpel 2")) {
            this.id_ubicacion = 12;
        }
        if (nombre.equals("MASxMENOS")) {
            this.id_ubicacion = 13;
        }
        if (nombre.equals("Iglesia 1")) {
            this.id_ubicacion = 14;
        }
        if (nombre.equals("Don Pancho")) {
            this.id_ubicacion = 15;
        }
        if (nombre.equals("Taller de Motos")) {
            this.id_ubicacion = 16;
        }
        if (nombre.equals("El Descanso")) {
            this.id_ubicacion = 17;
        }
        if (nombre.equals("Droguería")) {
            this.id_ubicacion = 18;
        }
        if (nombre.equals("Clínica")) {
            this.id_ubicacion = 19;
        }
        if (nombre.equals("Terminal")) {
            this.id_ubicacion = 20;
        }
        if (nombre.equals("DIAN")) {
            this.id_ubicacion = 21;
        }
        if (nombre.equals("Fiscalia")) {
            this.id_ubicacion = 22;
        }
        if (nombre.equals("D1")) {
            this.id_ubicacion = 23;
        }
        if (nombre.equals("ARA")) {
            this.id_ubicacion = 24;
        }
        if (nombre.equals("Drogueria 2")) {
            this.id_ubicacion = 25;
        }
        if (nombre.equals("Isimo")) {
            this.id_ubicacion = 26;
        }
    }

    public void pintarNodo(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // alta definición
        Color COLOR_NODO = new Color(0, 196, 255);
        graphics.setColor(COLOR_NODO); // asigna colores personalizados
        graphics.fillOval(this.getX() - getD() / 2, this.getY() - getD() / 2, getD(), getD()); // relleno del círculo

        graphics.setColor(Color.blue);
        graphics.drawString(this.getNombreN(), this.getX() - getD() / 1, this.getY() + getD() / 1); // *** nombre del vértice
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getid_ubicacion() {
        return id_ubicacion;
    }

    public String getNombreN() {
        return nombreN;
    }

    public static int getD() {
        return d;
    }
}
