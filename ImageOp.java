import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ImageOp { 

    public static void operate(String keyText, boolean encrypt) {
        JFileChooser f = new JFileChooser(); 
        f.showOpenDialog(null); 
        File file = f.getSelectedFile(); 

        try { 
            // Read file data
            FileInputStream fis = new FileInputStream(file); 
            byte[] data = new byte[fis.available()]; 
            fis.read(data); 
            fis.close();

            // AES requires a 16-byte key
            byte[] keyBytes = new byte[16];
            byte[] passwordBytes = keyText.getBytes("UTF-8");
            System.arraycopy(passwordBytes, 0, keyBytes, 0, Math.min(passwordBytes.length, keyBytes.length));
            
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            // Select mode (Encrypt/Decrypt)
            Cipher cipher = Cipher.getInstance("AES");
            if (encrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }

            // Process file
            byte[] outputBytes = cipher.doFinal(data);

            // Write back
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(outputBytes);
            fos.close();

            JOptionPane.showMessageDialog(null, "Done (" + (encrypt ? "Encrypted" : "Decrypted") + ")"); 

        } catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 

    public static void main(String args[]) { 
        System.out.println("This is testing"); 
         
        JFrame f = new JFrame(); 
        f.setTitle("Encrypt/Decrypt File"); 
        f.setSize(400,200); 
        f.setLocationRelativeTo(null); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        Font font = new Font("Roboto", Font.BOLD, 18); 

        JButton encryptButton = new JButton("Encrypt"); 
        encryptButton.setFont(font);

        JButton decryptButton = new JButton("Decrypt"); 
        decryptButton.setFont(font);

        JTextField textField = new JTextField(15); 
        textField.setFont(font); 

        encryptButton.addActionListener(e -> { 
            String text = textField.getText(); 
            if (!text.isEmpty()) {
                operate(text, true); 
            }
        }); 

        decryptButton.addActionListener(e -> { 
            String text = textField.getText(); 
            if (!text.isEmpty()) {
                operate(text, false); 
            }
        }); 

        f.setLayout(new FlowLayout()); 
        f.add(textField); 
        f.add(encryptButton); 
        f.add(decryptButton); 
        f.setVisible(true); 
    } 
}
