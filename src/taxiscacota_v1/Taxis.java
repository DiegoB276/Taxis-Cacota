/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxiscacota_v1;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Mariana
 */
public class Taxis {

    private int x;
    private int y;
    static final int d = 20;
    Image imagen;

    public Taxis(int x, int y) {
        this.x = x;
        this.y = y;
        imagen = new ImageIcon("taxi.png").getImage();
    }

    public void pintarTaxi(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        g.drawImage(imagen, this.x - getD() / 2, this.y - getD() / 2, null);
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
}
