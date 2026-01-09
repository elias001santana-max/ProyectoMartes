package BD;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class Barraa {

    public JFrame frame;
    private final RoundedProgressBar progressBar = new RoundedProgressBar();
    private Thread hiloCarga;

    private String nombreUsuario;

    // COLORES DEL MISMO DISEÑO DEL LOGIN
    private final Color BLUE_DARK = new Color(30, 41, 59);
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color ACCENT_COLOR = new Color(251, 146, 60);
    private final Color CARD_COLOR = Color.WHITE;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Barraa window = new Barraa();
                window.startCarga();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Barraa() {
        initialize();
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    private void initialize() {

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(900, 560);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // REDONDEAR ESQUINAS DE TODA LA VENTANA
        frame.setShape(new RoundRectangle2D.Double(0, 0, 900, 560, 30, 30));

        JPanel panelFondo = new JPanel();
        panelFondo.setBackground(new Color(0, 0, 51));
        panelFondo.setBounds(0, 0, 900, 560);
        panelFondo.setLayout(null);
        frame.getContentPane().add(panelFondo);

        // BARRA DE PROGRESO
        progressBar.setBounds(239, 483, 431, 53);
        panelFondo.add(progressBar);

        progressBar.setFont(new Font("Cambria", Font.BOLD, 25));
        progressBar.setForeground(PRIMARY_COLOR);
        progressBar.setBackground(Color.WHITE);
        progressBar.setStringPainted(true);

        JLabel lblCargando = new JLabel("Cargando");
        lblCargando.setBounds(275, 414, 359, 58);
        lblCargando.setFont(new Font("Cambria", Font.BOLD, 40));
        lblCargando.setForeground(Color.WHITE);
        lblCargando.setHorizontalAlignment(SwingConstants.CENTER);
        panelFondo.add(lblCargando);

        JLabel lblGif = new JLabel();
        lblGif.setIcon(new ImageIcon(
                Barraa.class.getResource("/BD/Video_de_playa_con_olas_y_palmeras-ezgif.com-optimize.gif")));
        lblGif.setBounds(0, 74, 900, 340);
        panelFondo.add(lblGif);
    }

    // Barra de carga
    public void startCarga() {

        if (hiloCarga != null && hiloCarga.isAlive()) return;

        hiloCarga = new Thread(() -> {
            frame.setVisible(true);

            for (int v = 0; v <= 100; v++) {
                int valor = v;
                SwingUtilities.invokeLater(() -> progressBar.setValue(valor));

                try {
                    Thread.sleep(45);
                } catch (InterruptedException e) {
                    return;
                }
            }

            SwingUtilities.invokeLater(() -> {
                frame.dispose();

                INTERFAZ ventana = new INTERFAZ();
                ventana.setNombreUsuario(nombreUsuario);
                ventana.frame.setVisible(true);
            });
        });

        hiloCarga.start();
    }

    // BARRA DE PROGRESO REDONDEADA
    class RoundedProgressBar extends JProgressBar {

        private static final int CORNER_RADIUS = 26;

        public RoundedProgressBar() {
            setOpaque(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            RoundRectangle2D backgroundShape =
                    new RoundRectangle2D.Double(0, 0, width - 1, height - 1, CORNER_RADIUS, CORNER_RADIUS);

            g2.setColor(getBackground());
            g2.fill(backgroundShape);

            if (getValue() > 0) {
                double percent = getPercentComplete();
                int progressWidth = (int) ((width - 6) * percent);

                if (progressWidth > CORNER_RADIUS) {
                    RoundRectangle2D progressShape =
                            new RoundRectangle2D.Double(3, 3, progressWidth, height - 6,
                                    CORNER_RADIUS - 4, CORNER_RADIUS - 4);
                    g2.setColor(getForeground());
                    g2.fill(progressShape);
                }
            }

            g2.setColor(new Color(148, 163, 184));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(backgroundShape);

            if (isStringPainted()) {
                String text = getString();
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();

                int x = (width - fm.stringWidth(text)) / 2;
                int y = (height + fm.getAscent()) / 2 - 2;

                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawString(text, x + 1, y + 1);

                g2.setColor(Color.DARK_GRAY);
                g2.drawString(text, x, y);
            }

            g2.dispose();
        }
    }
}
