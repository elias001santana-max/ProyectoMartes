package BD;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import IA.NlpEngine;
import IA.IaResponse;

public class PanelGestionIA extends JPanel {
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton sendButton;
    private final NlpEngine nlpEngine = new NlpEngine();

    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        
        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius/2;
            return insets;
        }
    }
    
    static class RoundedButtonBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        
        public RoundedButtonBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color.darker());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/3, radius/3, radius/3, radius/3);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius/3;
            return insets;
        }
    }
    
    static class RoundedLineBorder extends LineBorder {
        private final int radius;
        public RoundedLineBorder(Color color, int thickness, int radius) {
            super(color, thickness, true);
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(lineColor);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness/2, y + thickness/2, width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
    }

    public PanelGestionIA() {
        UIManager.put("Button.background", new Color(59, 130, 246));
        UIManager.put("Button.foreground", Color.WHITE);
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 246, 250));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("🤖 Asistente de IA - Chat");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(30, 58, 95));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(245, 246, 250));

        addMessage("🤖 Asistente IA: ¡Hola! Soy tu asistente de gestión hotelera.", false);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel inputPanelWrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 246, 250));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        inputPanelWrapper.setLayout(new BoxLayout(inputPanelWrapper, BoxLayout.Y_AXIS));
        inputPanelWrapper.setBackground(new Color(245, 246, 250));
        inputPanelWrapper.setBorder(new EmptyBorder(0, 0, 40, 0));

        JPanel inputPanel = new JPanel(new BorderLayout(0, 0));
        inputPanel.setBackground(new Color(245, 246, 250));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(200, 200, 220), 18),
            new EmptyBorder(12, 18, 12, 0)
        ));
        inputField.setToolTipText("Escribe tu pregunta para la IA...");
        
        int fieldHeight = inputField.getPreferredSize().height;

        sendButton = new JButton() {
            private Color normalColor = new Color(59, 130, 246);
            private Color hoverColor = new Color(37, 99, 235);
            private Color currentColor = normalColor;
            
            {
                setText("Enviar");
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setForeground(Color.WHITE);
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setOpaque(true);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(90, fieldHeight));
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        currentColor = hoverColor;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        currentColor = normalColor;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(currentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                
                g2.setColor(currentColor.darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(text, x, y);
                
                g2.dispose();
            }
            
            @Override
            public void setBackground(Color bg) {
                if (bg.equals(normalColor) || bg.equals(hoverColor)) {
                    super.setBackground(bg);
                    currentColor = bg;
                }
            }
        };

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        buttonPanel.add(sendButton);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        inputPanelWrapper.add(inputPanel);

        add(titulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanelWrapper, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> enviarMensaje());
        inputField.addActionListener(e -> enviarMensaje());
    }

    public void realizarBusquedaIA(String query) {
        if (query != null && !query.trim().isEmpty() && !query.contains("Buscar")) {
            SwingUtilities.invokeLater(() -> {
                inputField.setText(query);
                enviarMensaje();
            });
        }
    }

    private void enviarMensaje() {
        String mensaje = inputField.getText().trim();
        if (!mensaje.isEmpty()) {
            addMessage("👤 Tú: " + mensaje, true);
            inputField.setText("");

            String respuestaIA = procesarConTuIA(mensaje);
            addMessage("🤖 IA: " + respuestaIA, false);

            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }
    }

    /**
     * CORRECCIÓN APLICADA: Solo retorna el mensaje de la IA
     * sin agregar entidades adicionales que causaban duplicación
     */
    private String procesarConTuIA(String mensaje) {
        try {
            IaResponse respuesta = nlpEngine.process(mensaje);
            if (respuesta == null) return "No entendí tu pregunta.";
            
            // SOLO retornar el mensaje formateado de la IA
            return respuesta.getMessage() != null ? respuesta.getMessage().trim() : "Sin respuesta";
            
        } catch (Exception e) {
            return "Ocurrió un error al procesar tu mensaje.";
        }
    }

    private void addMessage(String texto, boolean esUsuario) {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);

        JTextArea messageLabel = new JTextArea(texto);
        messageLabel.setEditable(false);
        messageLabel.setLineWrap(true);
        messageLabel.setWrapStyleWord(true);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedLineBorder(esUsuario ? new Color(59,130,246) : new Color(200,200,220), 4, 18),
            new EmptyBorder(8, 64, 8, 64)
        ));
        messageLabel.setBackground(Color.WHITE);
        messageLabel.setOpaque(true);
        messageLabel.setAlignmentX(esUsuario ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        if (!esUsuario) {
            messageLabel.setBackground(new Color(235, 245, 255));
        }

        JPanel alignPanel = new JPanel(new FlowLayout(esUsuario ? FlowLayout.RIGHT : FlowLayout.LEFT));
        alignPanel.setOpaque(false);
        alignPanel.add(messageLabel);

        messagePanel.add(alignPanel, BorderLayout.CENTER);
        chatPanel.add(messagePanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }
}