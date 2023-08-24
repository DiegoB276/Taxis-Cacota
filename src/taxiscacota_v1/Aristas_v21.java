/* UNIVERSIDAD DE PAMPLONA
    Ingeniería de Sistemas - Matemáticas discretas: Grafos
    M.Sc. Luis Armando Portilla Granados
    Actualizado octubre de 2022: ArrayList
 */
package taxiscacota_v1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author Mariana
 */
public class Aristas_v21 {

    private Nodos_v21 nodo1, nodo2;
    private int valorDistancia;

    public Aristas_v21(Nodos_v21 nodo1, Nodos_v21 nodo2, int distancia) {
        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.valorDistancia = distancia;
    }

    public void pintarArista(Graphics g) { //(Graphics2D g)
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //SUAVISADO
        graphics.setColor(Color.GRAY);
        graphics.setStroke(new BasicStroke(2)); // Graphics2D grosor de línea
        graphics.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());

        // Dibujar el valor de distancia al centro de la línea
        int centerX = (this.getN1().getX() + this.getN2().getX()) / 2;
        int centerY = (this.getN1().getY() + this.getN2().getY()) / 2;
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Consolas", Font.PLAIN, 13));
        String distanciaStr = String.valueOf(valorDistancia);
        graphics.drawString(distanciaStr, centerX, centerY);

    }

    public void pintarAristaNaranja(Graphics g) { //(Graphics2D g)
        Graphics2D graphics = (Graphics2D) g;
        // http://swing-facil.blogspot.com/2011/12/renderinghints-renderizados-y.html
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //SUAVISADO
        graphics.setColor(Color.ORANGE);
        graphics.setStroke(new BasicStroke(4)); // Graphics2D grosor de línea
        graphics.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());

        // Dibujar el valor de distancia al centro de la línea
        int centerX = (this.getN1().getX() + this.getN2().getX()) / 2;
        int centerY = (this.getN1().getY() + this.getN2().getY()) / 2;
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Consolas", Font.PLAIN, 13));
        String distanciaStr = String.valueOf(valorDistancia);
        graphics.drawString(distanciaStr, centerX, centerY);

    }

    public void pintarAristaD(Graphics g) { //(Graphics2D g)
        Graphics2D graphics = (Graphics2D) g;
        // http://swing-facil.blogspot.com/2011/12/renderinghints-renderizados-y.html
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //SUAVISADO
        graphics.setColor(Color.RED);
        int grosor = 4;
        int ancho = 4;
        float dash[] = {ancho};
        graphics.setStroke(new BasicStroke(grosor, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f));
        graphics.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());

        // Dibujar el valor de distancia al centro de la línea
        int centerX = (this.getN1().getX() + this.getN2().getX()) / 2;
        int centerY = (this.getN1().getY() + this.getN2().getY()) / 2;
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Consolas", Font.PLAIN, 13));
        String distanciaStr = String.valueOf(valorDistancia);
        graphics.drawString(distanciaStr, centerX, centerY);

    }

    public void repintarArista(Graphics g) { //(Graphics2D g)
        g.setColor(Color.RED);
        g.drawLine(this.getN1().getX(), this.getN1().getY(), this.getN2().getX(), this.getN2().getY());
    }

    public Nodos_v21 getN1() {
        return nodo1;
    }

    public Nodos_v21 getN2() {
        return nodo2;
    }

    public int getValorDistancia() {
        return valorDistancia;
    }
}
