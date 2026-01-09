package BD;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.util.Base64;
import java.sql.*;

public class Registro {

public JFrame frame;
private RoundedTextField txtUsername;
private RoundedPasswordField txtPassword;
private RoundedTextField txtNombre;
private JLabel lblTogglePassword;
private boolean mostrarPassword = false;

// COLORES - IGUALES A LOGIN
private final Color PRIMARY_COLOR = new Color(59, 130, 246);
private final Color ACCENT_COLOR = new Color(251, 146, 60);
private final Color BG_COLOR = new Color(30, 41, 59);
private final Color CARD_COLOR = Color.WHITE;
private final Color TEXT_DARK = new Color(30, 41, 59);

public Registro() {
initialize();
}

private void initialize() {

frame = new JFrame();
frame.getContentPane().setForeground(new Color(0, 0, 102));
frame.setUndecorated(true);
frame.setSize(900, 560);
frame.setLocationRelativeTo(null);
frame.getContentPane().setBackground(new Color(0, 0, 51));
frame.getContentPane().setLayout(null);

// REDONDEAR ESQUINAS DE TODA LA VENTANA
frame.setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 900, 560, 30, 30));

// BOTÓN CERRAR - SOLO X ROJA SIN CUADRO
JButton btnCerrar = new JButton("X");
btnCerrar.setBounds(810, 11, 55, 62);
btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
btnCerrar.setForeground(new Color(239, 68, 68)); // Texto rojo
btnCerrar.setContentAreaFilled(false); // Sin fondo
btnCerrar.setBorderPainted(false); // Sin borde
btnCerrar.setFocusPainted(false); // Sin borde de foco
btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// Efecto hover para oscurecer la X al pasar el mouse
btnCerrar.addMouseListener(new MouseAdapter() {
@Override
public void mouseEntered(MouseEvent e) {
btnCerrar.setForeground(new Color(185, 28, 28)); // Rojo más oscuro
}

@Override
public void mouseExited(MouseEvent e) {
btnCerrar.setForeground(new Color(239, 68, 68)); // Rojo normal
}
});

btnCerrar.addActionListener(e -> volverAlLogin());
frame.getContentPane().add(btnCerrar);

// TARJETA CON BORDES BIEN REDONDEADOS
JPanel tarjeta = new RoundedPanel(30);
tarjeta.setBounds(70, 26, 364, 504);
tarjeta.setBackground(CARD_COLOR);
tarjeta.setLayout(null);
frame.getContentPane().add(tarjeta);

// LOGO DEL HOTEL
JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
lblTitulo.setIcon(new ImageIcon(Registro.class.getResource("/BD/mascota_231x203.png")));
lblTitulo.setBounds(48, 32, 231, 180);
tarjeta.add(lblTitulo);

// CAMPO NOMBRE CON BORDES REDONDEADOS
JLabel lblNombre = new JLabel("Nombre");
lblNombre.setFont(new Font("Cambria", Font.BOLD, 18));
lblNombre.setBounds(40, 225, 150, 25);
tarjeta.add(lblNombre);

txtNombre = new RoundedTextField(15);
txtNombre.setBounds(40, 250, 280, 35);
txtNombre.setFont(new Font("Cambria", Font.PLAIN, 14));
txtNombre.setBorderColor(Color.LIGHT_GRAY);
tarjeta.add(txtNombre);

// CAMPO USUARIO CON BORDES REDONDEADOS
JLabel lblUsuario = new JLabel("Usuario");
lblUsuario.setFont(new Font("Cambria", Font.BOLD, 18));
lblUsuario.setBounds(40, 290, 100, 25);
tarjeta.add(lblUsuario);

txtUsername = new RoundedTextField(15);
txtUsername.setBounds(40, 315, 280, 35);
txtUsername.setFont(new Font("Cambria", Font.PLAIN, 14));
txtUsername.setBorderColor(Color.LIGHT_GRAY);
tarjeta.add(txtUsername);

// CAMPO CONTRASEÑA - SOLO 6 DÍGITOS
JLabel lblPass = new JLabel("Contraseña (6 dígitos)");
lblPass.setFont(new Font("Cambria", Font.BOLD, 18));
lblPass.setBounds(40, 355, 250, 25);
tarjeta.add(lblPass);

// PANEL CONTENEDOR PARA CONTRASEÑA + ÍCONO
JPanel passwordPanel = new JPanel(null);
passwordPanel.setBounds(40, 380, 280, 35);
passwordPanel.setOpaque(false);

txtPassword = new RoundedPasswordField(15);
txtPassword.setBounds(0, 0, 245, 35);
txtPassword.setFont(new Font("Cambria", Font.PLAIN, 14));
txtPassword.setBorderColor(Color.LIGHT_GRAY);

// APLICAR FILTRO PARA SOLO 6 DÍGITOS
((AbstractDocument) txtPassword.getDocument()).setDocumentFilter(new DocumentFilter() {
@Override
public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
throws BadLocationException {
if (string == null) return;

// Solo permitir dígitos
if (string.matches("\\d+")) {
// Solo insertar si no excede 6 caracteres
if ((fb.getDocument().getLength() + string.length()) <= 6) {
super.insertString(fb, offset, string, attr);
}
}
}

@Override
public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
throws BadLocationException {
if (text == null) return;

// Solo permitir dígitos
if (text.matches("\\d+")) {
// Calcular nueva longitud después del reemplazo
int currentLength = fb.getDocument().getLength();
int newLength = currentLength - length + text.length();

// Solo reemplazar si no excede 6 caracteres
if (newLength <= 6) {
super.replace(fb, offset, length, text, attrs);
}
}
}

@Override
public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
super.remove(fb, offset, length);
}
});

passwordPanel.add(txtPassword);

// ICONO DE OJO PARA MOSTRAR/OCULTAR CONTRASEÑA
lblTogglePassword = new JLabel();
lblTogglePassword.setBounds(250, 7, 25, 21);
lblTogglePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

// POR DEFECTO: OJO TACHADO (contraseña oculta con puntitos)
try {
// Intentar cargar imagen del ojo tachado
ImageIcon eyeOffIcon = new ImageIcon(Registro.class.getResource("/BD/eye_off_icon.png"));
Image eyeOffImg = eyeOffIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
lblTogglePassword.setIcon(new ImageIcon(eyeOffImg));
} catch (Exception e) {
// Fallback: usar Unicode de ojo tachado
lblTogglePassword.setText("🙈");
lblTogglePassword.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
}

// Agregar listener para toggle
lblTogglePassword.addMouseListener(new MouseAdapter() {
@Override
public void mouseClicked(MouseEvent e) {
togglePasswordVisibility();
}

@Override
public void mouseEntered(MouseEvent e) {
lblTogglePassword.setForeground(PRIMARY_COLOR);
}

@Override
public void mouseExited(MouseEvent e) {
lblTogglePassword.setForeground(Color.GRAY);
}
});

passwordPanel.add(lblTogglePassword);
tarjeta.add(passwordPanel);

// BOTÓN REDONDEADO "REGISTRAR"
RoundedButton btnRegistrar = new RoundedButton("REGISTRAR", 20);
btnRegistrar.setFont(new Font("Cambria", Font.PLAIN, 22));
btnRegistrar.setBounds(90, 440, 170, 40);
btnRegistrar.setBackground(PRIMARY_COLOR);
btnRegistrar.setForeground(Color.WHITE);
btnRegistrar.setFocusPainted(false);
btnRegistrar.setBorderPainted(false);
btnRegistrar.setContentAreaFilled(false);
btnRegistrar.addActionListener(e -> registrar());
tarjeta.add(btnRegistrar);

JLabel lblNewLabel = new JLabel("");
lblNewLabel.setBackground(new Color(0, 0, 102));
lblNewLabel.setIcon(new ImageIcon(Registro.class.getResource("/BD/Imagen_a_Video_Generado-ezgif.com-optimize (1).gif")));
lblNewLabel.setBounds(0, 104, 910, 313);
frame.getContentPane().add(lblNewLabel);

JLabel lblNewLabel_1 = new JLabel("Que esperas empieza ");
lblNewLabel_1.setFont(new Font("Cambria", Font.PLAIN, 35));
lblNewLabel_1.setForeground(new Color(255, 255, 255));
lblNewLabel_1.setBounds(492, 428, 387, 62);
frame.getContentPane().add(lblNewLabel_1);

JLabel lblNewLabel_1_1 = new JLabel("Hoy");
lblNewLabel_1_1.setForeground(Color.WHITE);
lblNewLabel_1_1.setFont(new Font("Cambria", Font.PLAIN, 35));
lblNewLabel_1_1.setBounds(629, 476, 203, 51);
frame.getContentPane().add(lblNewLabel_1_1);
}

// TOGGLE MOSTRAR/OCULTAR CONTRASEÑA
private void togglePasswordVisibility() {
mostrarPassword = !mostrarPassword;

if (mostrarPassword) {
// MOSTRAR CONTRASEÑA (números visibles)
txtPassword.setEchoChar((char) 0);

// Cambiar a OJO NORMAL (sin tache) porque ahora SÍ se ve
try {
ImageIcon eyeIcon = new ImageIcon(Registro.class.getResource("/BD/eye_icon.png"));
Image eyeImg = eyeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
lblTogglePassword.setIcon(new ImageIcon(eyeImg));
} catch (Exception e) {
// Fallback: emoji de ojo normal
lblTogglePassword.setText("👁");
}
} else {
// OCULTAR CONTRASEÑA (puntitos)
txtPassword.setEchoChar('•');

// Cambiar a OJO TACHADO porque NO se ve
try {
ImageIcon eyeOffIcon = new ImageIcon(Registro.class.getResource("/BD/eye_off_icon.png"));
Image eyeOffImg = eyeOffIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
lblTogglePassword.setIcon(new ImageIcon(eyeOffImg));
} catch (Exception e) {
// Fallback: emoji de ojo tachado
lblTogglePassword.setText("🙈");
}
}
}

// REGISTRAR USUARIO Y VOLVER AL LOGIN
private void registrar() {
String user = txtUsername.getText().trim();
String pass = new String(txtPassword.getPassword()).trim();
String nombre = txtNombre.getText().trim();

if (user.isEmpty() || pass.isEmpty() || nombre.isEmpty()) {
JOptionPane.showMessageDialog(frame, "Llena todos los campos");
return;
}

// VALIDAR QUE LA CONTRASEÑA TENGA EXACTAMENTE 6 DÍGITOS
if (pass.length() != 6) {
JOptionPane.showMessageDialog(frame,
"La contraseña debe tener exactamente 6 dígitos",
"Error de validación",
JOptionPane.ERROR_MESSAGE);
return;
}

if (!pass.matches("\\d{6}")) {
JOptionPane.showMessageDialog(frame,
"La contraseña debe contener solo números",
"Error de validación",
JOptionPane.ERROR_MESSAGE);
return;
}

// Verificar usuario existente en la base de datos
if (usuarioExiste(user)) {
JOptionPane.showMessageDialog(frame, "Ese usuario ya existe");
return;
}

// Encriptar contraseña
String passEncriptada = encryptPassword(pass);

// Guardar en la base de datos
if (guardarEnBaseDeDatos(user, passEncriptada, nombre)) {
// Guardar en memoria también
Usuario nuevo = new Usuario(user, passEncriptada, nombre);
Loginn.usuariosEnMemoria.add(nuevo);

// Ir directamente al Login sin mensaje
volverAlLogin();
} else {
JOptionPane.showMessageDialog(frame,
"Error al registrar usuario. Intenta de nuevo.");
}
}

// Verificar si el usuario ya existe en la BD
private boolean usuarioExiste(String usuario) {
String url = "jdbc:sqlite:C:/Users/usuario/Desktop/ProyectoMartes/BDHotel.db";
String query = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";

try (Connection con = DriverManager.getConnection(url);
PreparedStatement pstmt = con.prepareStatement(query)) {

pstmt.setString(1, usuario);
ResultSet rs = pstmt.executeQuery();

if (rs.next()) {
return rs.getInt(1) > 0;
}
} catch (SQLException e) {
e.printStackTrace();
}
return false;
}

// Guardar usuario en la base de datos
private boolean guardarEnBaseDeDatos(String usuario, String password, String nombre) {
String url = "jdbc:sqlite:C:/Users/usuario/Desktop/ProyectoMartes/BDHotel.db";
String insert = "INSERT INTO usuarios (usuario, password, nombre) VALUES (?, ?, ?)";

try (Connection con = DriverManager.getConnection(url);
PreparedStatement pstmt = con.prepareStatement(insert)) {

pstmt.setString(1, usuario);
pstmt.setString(2, password);
pstmt.setString(3, nombre);
pstmt.executeUpdate();

return true;
} catch (SQLException e) {
e.printStackTrace();
return false;
}
}

// Volver al Login
private void volverAlLogin() {
Loginn login = new Loginn();
login.frmLogin.setVisible(true);
frame.dispose();
}

// Encriptar contraseña
public static String encryptPassword(String password) {
try {
MessageDigest md = MessageDigest.getInstance("SHA-256");
return Base64.getEncoder().encodeToString(md.digest(password.getBytes("UTF-8")));
} catch (Exception e) {
return null;
}
}

// PANEL CON BORDES REDONDEADOS
class RoundedPanel extends JPanel {
private int cornerRadius;

public RoundedPanel(int radius) {
super();
this.cornerRadius = radius;
setOpaque(false);
}

@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
g2.setColor(getBackground());
g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
g2.setColor(Color.LIGHT_GRAY);
g2.setStroke(new BasicStroke(1));
g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
g2.dispose();
super.paintComponent(g);
}
}

// BOTÓN CON BORDES REDONDEADOS
class RoundedButton extends JButton {
private int cornerRadius;
private Color hoverColor;
private boolean isHovering = false;

public RoundedButton(String text, int radius) {
super(text);
this.cornerRadius = radius;
setOpaque(false);
setFocusPainted(false);
setBorderPainted(false);
setContentAreaFilled(false);

addMouseListener(new MouseAdapter() {
@Override
public void mouseEntered(MouseEvent e) {
isHovering = true;
hoverColor = getBackground().darker();
repaint();
}

@Override
public void mouseExited(MouseEvent e) {
isHovering = false;
repaint();
}
});
}

@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
if (isHovering && hoverColor != null) {
g2.setColor(hoverColor);
} else {
g2.setColor(getBackground());
}
g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
g2.dispose();
super.paintComponent(g);
}
}

// TEXTFIELD CON BORDES REDONDEADOS
class RoundedTextField extends JTextField {
private int cornerRadius;
private Color borderColor = Color.LIGHT_GRAY;

public RoundedTextField(int radius) {
super();
this.cornerRadius = radius;
setOpaque(false);
setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
}

public void setBorderColor(Color color) {
this.borderColor = color;
repaint();
}

@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

// Dibujar fondo blanco
g2.setColor(Color.WHITE);
g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

g2.dispose();
super.paintComponent(g);
}

@Override
protected void paintBorder(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

// Dibujar borde
g2.setColor(borderColor);
g2.setStroke(new BasicStroke(2));
g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);

g2.dispose();
}
}

// PASSWORDFIELD CON BORDES REDONDEADOS
class RoundedPasswordField extends JPasswordField {
private int cornerRadius;
private Color borderColor = Color.LIGHT_GRAY;

public RoundedPasswordField(int radius) {
super();
this.cornerRadius = radius;
setOpaque(false);
setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
}

public void setBorderColor(Color color) {
this.borderColor = color;
repaint();
}

@Override
protected void paintComponent(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

// Dibujar fondo blanco
g2.setColor(Color.WHITE);
g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

g2.dispose();
super.paintComponent(g);
}

@Override
protected void paintBorder(Graphics g) {
Graphics2D g2 = (Graphics2D) g.create();
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

// Dibujar borde
g2.setColor(borderColor);
g2.setStroke(new BasicStroke(2));
g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);

g2.dispose();
}
}
}
