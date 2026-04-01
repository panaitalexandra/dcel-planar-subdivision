import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Drawing extends Canvas {
    private int mouseX, mouseY, MMx, MMy;
    private double Mx, MMMx;
    private double My, MMMy;
    private JTextField output;
    private Algorithm a;
    private int F;

    private boolean drawPoli;
    private boolean mouseSelect;
    private boolean mouseStart;

    public Drawing() {
        setSize(500, 500);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawLine(0, 0, 500, 0);
        g2.drawLine(0, 0, 0, 500);
        g2.drawLine(500, 0, 500, 500);
        g2.drawLine(0, 500, 500, 500);

        g2.drawLine(calcX(-20), calcY(0), calcX(20), calcY(0));
        g2.drawString("x", calcX(21), calcY(-1));
        g2.drawLine(calcX(20) - 3, calcY(0) - 3, calcX(20), calcY(0));
        g2.drawLine(calcX(20) - 3, calcY(0) + 3, calcX(20), calcY(0));

        g2.drawLine(calcX(0), calcY(-20), calcX(0), calcY(20));
        g2.drawString("y", calcX(-1), calcY(21));
        g2.drawLine(calcX(0) + 3, calcY(20) + 3, calcX(0), calcY(20));
        g2.drawLine(calcX(0) - 3, calcY(20) + 3, calcX(0), calcY(20));

        if (drawPoli) {
            Edge[] dcel = a.getDCEL();
            for (int i = 0; i < dcel.length; ++i) {
                g2.drawLine(calcX((int) dcel[i].getV1().getX()),
                        calcY((int) dcel[i].getV1().getY()),
                        calcX((int) dcel[i].getV2().getX()),
                        calcY((int) dcel[i].getV2().getY())
                );
                int mijlocX = (calcX((int) dcel[i].getV1().getX()) + calcX((int) dcel[i].getV2().getX())) / 2;
                int mijlocY = (calcY((int) dcel[i].getV1().getY()) + calcY((int) dcel[i].getV2().getY())) / 2;
                g2.fillOval(calcX((int) dcel[i].getV1().getX()), calcY((int) dcel[i].getV1().getY()), 3, 3);
                g2.fillOval(calcX((int) dcel[i].getV2().getX()), calcY((int) dcel[i].getV2().getY()), 3, 3);

                double mX = (dcel[i].getV1().getX() + dcel[i].getV2().getX()) / 2.0;
                double mY = (dcel[i].getV1().getY() + dcel[i].getV2().getY()) / 2.0;
                double mmX = mX;
                double mmY = mY;
                double mp1X = mX - 0.3;
                double mp1Y = mY - 0.3;
                double mp2X = mX + 0.3;
                double mp2Y = mY - 0.3;
                double inclinare = 0d;

                if (dcel[i].getV1().getX() < dcel[i].getV2().getX())
                    inclinare = -Math.PI / 2;
                else
                    inclinare = Math.PI / 2;

                g2.setColor(Color.red);

                double teta = (Math.sqrt(Math.pow(dcel[i].getV1().getX()-mX, 2) + Math.pow(dcel[i].getV1().getY() - mY, 2)))/(Math.sqrt(Math.pow(dcel[i].getV1().getY()-mY,2)));
                double cosu = Math.sqrt(Math.cos(Math.pow(dcel[i].getV1().getY() - mY, 2))) / Math.sqrt((Math.pow(dcel[i].getV1().getX() - mX, 2)));
                double tangenta = (dcel[i].getV2().getY() - dcel[i].getV1().getY()) / (dcel[i].getV2().getX() - dcel[i].getV1().getX());

                teta = Math.atan(tangenta) + inclinare;
                AffineTransform at = new AffineTransform();
                mX -= mp1X;
                mY -= mp1Y;
                at.rotate(teta, mX, mY);

                double newX = at.getTranslateX();
                double newY = at.getTranslateY();
                newX += mp1X;
                newY += mp1Y;
                g2.drawLine(mijlocX, mijlocY, calcX(newX), calcY(newY));
                AffineTransform at1 = new AffineTransform();
                mmX -= mp2X;
                mmY -= mp2Y;
                at1.rotate(teta, mmX, mmY);
                newX = at1.getTranslateX();
                newY = at1.getTranslateY();
                newX += mp2X;
                newY += mp2Y;

                g2.drawLine(mijlocX, mijlocY, calcX(newX), calcY(newY));
                g2.drawLine(mijlocX,mijlocY,mijlocX+3,mijlocY+3);
                g2.drawLine(mijlocX,mijlocY,mijlocX-3,mijlocY+3);
                g2.drawString("" + dcel[i].getId(), mijlocX + 2, mijlocY + 2);
                g2.setColor(Color.black);
            }
        }
        if (mouseSelect) {
            g.setColor(Color.blue);
            g.fillOval(mouseX, mouseY, 1, 1);
            g.drawLine(mouseX + 2, mouseY - 2, mouseX - 2, mouseY + 2);
            g.drawLine(mouseX - 2, mouseY - 2, mouseX + 2, mouseY + 2);
            Edge[] dcel = a.getDCEL();
            for (int i = 0; i < dcel.length; ++i) {
                if ((dcel[i].getF1() == F) || (dcel[i].getF2() == F))
                    g.drawLine(calcX((int) dcel[i].getV1().getX()),
                            calcY((int) dcel[i].getV1().getY()),
                            calcX((int) dcel[i].getV2().getX()),
                            calcY((int) dcel[i].getV2().getY())
                    );
            }
        }
        g.drawString("( " + MMMx + "," + MMMy + " )", MMx, MMy);
    }

    public int calcX(int x) {
        return 250 + x * 10;
    }

    public int calcY(int y) {
        return 250 - y * 10;
    }

    public boolean mouseDown(Event e, int x, int y) {
        if (mouseStart) {
            mouseX = x;
            mouseY = y;
            Mx = (mouseX - 250) / 10.0;
            My = (250 - mouseY) / 10.0;
            mouseSelect = true;
            mouseStart = false;
            F = a.search(Mx, My);
            repaint();
        }
        return true;
    }

    public boolean mouseMove(Event e, int x, int y) {
        if (mouseStart) {
            MMx = x;
            MMy = y;
            MMMx = (MMx - 250) / 10.0;
            MMMy = (250 - MMy) / 10.0;
        }
        repaint();
        return true;
    }

    public void setOutput(JTextField out) {
        output = out;
    }

    public void setAlgoritm(Algorithm a) {
        this.a = a;
        drawPoli = true;
        mouseStart = true;
        repaint();
    }

    public double calcDet(double x1, double y1, double x2, double y2, double x3, double y3) {
        return x1 * y2 + x2 * y3 + x3 * y1 - y2 * x3 - y3 * x1 - y1 * x2;
    }

    public int calcX(double x) {
        return (int) (250 + x * 10);
    }

    public int calcY(double y) {
        return (int) (250 - y * 10);
    }
}