/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxiscacota_v1;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Mariana
 */
public class Destino {

    private int x;
    private int y;
    static final int d = 30;  // diámetro para el Circulo
    private int ub_d;
    Image imagen;

    public Destino(int x, int y, int ub) {
        this.x = x;
        this.y = y;
        this.ub_d = ub;
        imagen = new ImageIcon("Destino.png").getImage();
    }

    public void pintarDestino(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.drawImage(imagen, this.x - getD() / 2, this.y - getD(), null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getD() {
        return d;
    }

    public int getub_d() {
        return ub_d;
    }
}
