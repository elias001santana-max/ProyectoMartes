package BD;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.ArrayList;

public class Loginn {

public JFrame frmLogin;
private RoundedTextField txtUsername;
private RoundedPasswordField txtPassword;
private JLabel lblErrorMessage;
private JLabel lblTogglePassword;
private boolean mostrarPassword = false;

String nombreUsuario = "";

// USUARIOS EN MEMORIA
public static ArrayList<Usuario> usuariosEnMemoria = new ArrayList<>();
private DefaultTableModel modeloUsuarios;

// COLORES
private final Color PRIMARY_COLOR = new Color(59, 130, 246);
private final Color ACCENT_COLOR = new Color(251, 146, 60);
private final Color BG_COLOR = new Color(30, 41, 59);
private final Color CARD_COLOR = Color.WHITE;
private final Color TEXT_DARK = new Color(30, 41, 59);
private final Color TEXT_LIGHT = new Color(148, 163, 184);
private final Color ERROR_RED = new Color(220, 38, 38);

public static void main(String[] args) {
EventQueue.invokeLater(() -> {
verificarBaseDeDatos();
Loginn window = new Loginn();
window.frmLogin.setVisible(true);
});
}

// CREAR BD Y TABLAS
private static void verificarBaseDeDatos() {
String url = "jdbc:sqlite:C:/Users/usuario/Desktop/ProyectoMartes/BDHotel.db";

try (Connection con = DriverManager.getConnection(url);
Statement stmt = con.createStatement()) {

stmt.executeUpdate("""
CREATE TABLE IF NOT EXISTS usuarios (
id INTEGER PRIMARY KEY AUTOINCREMENT,
usuario TEXT UNIQUE,
password TEXT,
nombre TEXT
)
""");

} catch (SQLException e) {
e.printStackTrace();
}
}

public Loginn() {
initialize();
cargarUsuariosEnMemoria();
actualizarTablaUsuarios();
}

private void initialize() {

frmLogin = new JFrame();
frmLogin.setBackground(new Color(0, 0, 102));
frmLogin.getContentPane().setBackground(new Color(0, 0, 51));
frmLogin.setUndecorated(true);
frmLogin.setSize(900, 560);
frmLogin.setLocationRelativeTo(null);
frmLogin.getContentPane().setLayout(null);

// REDONDEAR ESQUINAS DE TODA LA VENTANA
frmLogin.setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, 900, 560, 30, 30));

// IMAGEN DE FONDO
JLabel lblFondo = new JLabel();
lblFondo.setBackground(new Color(0, 0, 102));
lblFondo.setForeground(new Color(0, 0, 102));
lblFondo.setBounds(0, 0, 900, 560);
try {
ImageIcon iconFondo = new ImageIcon(Loginn.class.getResource("/BD/fondo_hotel.jpg"));
Image imgFondo = iconFondo.getImage().getScaledInstance(900, 560, Image.SCALE_SMOOTH);
lblFondo.setIcon(new ImageIcon(Loginn.class.getResource("")));
} catch (Exception e) {
lblFondo.setBackground(BG_COLOR);
lblFondo.setOpaque(true);
}
lblFondo.setLayout(null);

// BOTÓN CERRAR - SOLO X ROJA SIN CUADRO
JButton btnCerrar = new JButton("X");
btnCerrar.setBounds(830, 11, 35, 30);
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

btnCerrar.addActionListener(e -> System.exit(0));
lblFondo.add(btnCerrar);

// TARJETA CON BORDES BIEN REDONDEADOS
JPanel tarjeta = new RoundedPanel(30);
tarjeta.setBounds(70, 26, 364, 504);
tarjeta.setBackground(CARD_COLOR);
tarjeta.setLayout(null);
lblFondo.add(tarjeta);

JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
lblTitulo.setIcon(new ImageIcon(Loginn.class.getResource("/BD/hotel_ventura_231x180.png")));
lblTitulo.setBounds(67, 34, 231, 180);
tarjeta.add(lblTitulo);

JLabel lblUsuario = new JLabel("Usuario");
lblUsuario.setFont(new Font("Cambria", Font.BOLD, 18));
lblUsuario.setBounds(40, 225, 100, 25);
tarjeta.add(lblUsuario);

// CAMPO DE USUARIO CON BORDES REDONDEADOS
txtUsername = new RoundedTextField(15);
txtUsername.setBounds(40, 250, 280, 35);
txtUsername.setFont(new Font("Cambria", Font.PLAIN, 14));
txtUsername.setBorderColor(Color.LIGHT_GRAY);
// Restablecer bordes al escribir
txtUsername.addKeyListener(new KeyAdapter() {
@Override
public void keyPressed(KeyEvent e) {
resetErrorState();
}
});
tarjeta.add(txtUsername);

JLabel lblPass = new JLabel("Contraseña (6 dígitos)");
lblPass.setFont(new Font("Cambria", Font.BOLD, 18));
lblPass.setBounds(40, 296, 250, 25);
tarjeta.add(lblPass);

// PANEL CONTENEDOR PARA CONTRASEÑA + ÍCONO
JPanel passwordPanel = new JPanel(null);
passwordPanel.setBounds(40, 330, 280, 35);
passwordPanel.setOpaque(false);

// CAMPO DE CONTRASEÑA CON BORDES REDONDEADOS
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

// Restablecer bordes al escribir
txtPassword.addKeyListener(new KeyAdapter() {
@Override
public void keyPressed(KeyEvent e) {
resetErrorState();
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
ImageIcon eyeOffIcon = new ImageIcon(Loginn.class.getResource("/BD/eye_off_icon.png"));
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

// LABEL PARA MENSAJE DE ERROR
lblErrorMessage = new JLabel("");
lblErrorMessage.setFont(new Font("Calibri", Font.PLAIN, 12));
lblErrorMessage.setForeground(ERROR_RED);
lblErrorMessage.setBounds(40, 368, 280, 20);
lblErrorMessage.setVisible(false);
tarjeta.add(lblErrorMessage);

// BOTÓN REDONDEADO "ENTRAR"
RoundedButton btnLogin = new RoundedButton("ENTRAR", 20);
btnLogin.setFont(new Font("Cambria", Font.PLAIN, 25));
btnLogin.setBounds(90, 405, 170, 40);
btnLogin.setBackground(PRIMARY_COLOR);
btnLogin.setForeground(Color.WHITE);
btnLogin.setFocusPainted(false);
btnLogin.setBorderPainted(false);
btnLogin.setContentAreaFilled(false);
btnLogin.addActionListener(e -> iniciarSesion());
tarjeta.add(btnLogin);

// BOTÓN REGISTRATE FUNCIONAL
JLabel lblRegistro = new JLabel("Registrate");
lblRegistro.setFont(new Font("Calibri", Font.PLAIN, 16));
lblRegistro.setBounds(137, 456, 200, 25);
lblRegistro.setForeground(ACCENT_COLOR);
lblRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
lblRegistro.addMouseListener(new MouseAdapter() {
@Override
public void mouseClicked(MouseEvent e) {
Registro registro = new Registro();
registro.frame.setVisible(true);
frmLogin.dispose();
}
});
tarjeta.add(lblRegistro);

frmLogin.getContentPane().add(lblFondo);

JLabel lblNewLabel = new JLabel("");
lblNewLabel.setIcon(new ImageIcon(Loginn.class.getResource("/BD/Video_de_playa_con_olas_y_palmeras-ezgif.com-optimize.gif")));
lblNewLabel.setBounds(0, 115, 900, 300);
frmLogin.getContentPane().add(lblNewLabel);

modeloUsuarios = new DefaultTableModel(
new Object[]{"Usuario", "Nombre"}, 0
);
}

// TOGGLE MOSTRAR/OCULTAR CONTRASEÑA
private void togglePasswordVisibility() {
mostrarPassword = !mostrarPassword;

if (mostrarPassword) {
// MOSTRAR CONTRASEÑA (números visibles)
txtPassword.setEchoChar((char) 0);

// Cambiar a OJO NORMAL (sin tache) porque ahora SÍ se ve
try {
ImageIcon eyeIcon = new ImageIcon(Loginn.class.getResource("/BD/eye_icon.png"));
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
ImageIcon eyeOffIcon = new ImageIcon(Loginn.class.getResource("/BD/eye_off_icon.png"));
Image eyeOffImg = eyeOffIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
lblTogglePassword.setIcon(new ImageIcon(eyeOffImg));
} catch (Exception e) {
// Fallback: emoji de ojo tachado
lblTogglePassword.setText("🙈");
}
}
}

// RESTABLECER ESTADO DE ERROR
private void resetErrorState() {
// Restablecer borde del campo de usuario
txtUsername.setBorderColor(Color.LIGHT_GRAY);

// Restablecer borde del campo de contraseña
txtPassword.setBorderColor(Color.LIGHT_GRAY);

// Ocultar mensaje de error
lblErrorMessage.setVisible(false);
}

// MOSTRAR ERROR VISUAL
private void mostrarErrorCredenciales() {
// Cambiar borde del campo de contraseña a ROJO
txtPassword.setBorderColor(ERROR_RED);

// Cambiar borde del campo de usuario a ROJO también
txtUsername.setBorderColor(ERROR_RED);

// Mostrar mensaje de error
lblErrorMessage.setText("⚠ Usuario o contraseña incorrectos");
lblErrorMessage.setVisible(true);

// Animar el campo para llamar la atención
Timer timer = new Timer(100, null);
timer.addActionListener(new ActionListener() {
int count = 0;
@Override
public void actionPerformed(ActionEvent e) {
if (count < 3) {
txtPassword.setBackground(count % 2 == 0 ? new Color(255, 240, 240) : Color.WHITE);
count++;
} else {
txtPassword.setBackground(Color.WHITE);
((Timer)e.getSource()).stop();
}
}
});
timer.start();
}

// CARGAR USUARIOS
private void cargarUsuariosEnMemoria() {
usuariosEnMemoria.clear();

try (Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/usuario/Desktop/ProyectoMartes/BDHotel.db");
Statement st = con.createStatement();
ResultSet rs = st.executeQuery("SELECT usuario, password, nombre FROM usuarios")) {

while (rs.next()) {
usuariosEnMemoria.add(
new Usuario(
rs.getString("usuario"),
rs.getString("password"),
rs.getString("nombre")
)
);
}
} catch (SQLException e) {
e.printStackTrace();
}
}

private void actualizarTablaUsuarios() {
modeloUsuarios.setRowCount(0);
for (Usuario u : usuariosEnMemoria) {
modeloUsuarios.addRow(new Object[]{u.getUsuario(), u.getNombre()});
}
}

private void iniciarSesion() {
String user = txtUsername.getText().trim();
String pass = new String(txtPassword.getPassword()).trim();

// VALIDAR QUE LA CONTRASEÑA TENGA 6 DÍGITOS
if (pass.length() != 6) {
mostrarErrorCredenciales();
return;
}

// Encriptar la contraseña ingresada
String passEncriptada = encryptPassword(pass);

for (Usuario u : usuariosEnMemoria) {
if (u.getUsuario().equals(user) && u.getPassword().equals(passEncriptada)) {
// Abrir pantalla de carga Barraa
Barraa barraa = new Barraa();
barraa.setNombreUsuario(u.getNombre());
barraa.startCarga();
frmLogin.dispose();
return;
}
}

// SI LLEGAMOS AQUÍ, LAS CREDENCIALES SON INCORRECTAS
mostrarErrorCredenciales();
}

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