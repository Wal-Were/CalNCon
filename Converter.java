import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private JFrame frame;
    private JTextField inputField;
    private JTextField outputField;
    private boolean binaryToHex = true;
    private List<JButton> btnDigits = new ArrayList<>();

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Converter window = new Converter();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Converter() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 750, 500); // Adjust the size here
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        frame.getContentPane().setBackground(new Color(11, 91, 91));

        inputField = new JTextField();
        inputField.setBounds(34, 36, 146, 26);
        frame.getContentPane().add(inputField);
        inputField.setColumns(10);

        JLabel lblInput = new JLabel("Binary Input");
        lblInput.setBounds(34, 10, 1000, 35);
        frame.getContentPane().add(lblInput);
        lblInput.setForeground(Color.WHITE);

        // Add "switch" button at the bottom
        JButton switchButton = new JButton("Switch");
        switchButton.setBounds(294, 120, 80, 29); // Adjust position here
        switchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current JFrame
                frame.dispose();

                // Start the Calculator program
                String[] args = {};
                Calculator.main(args);
            }
        });
        switchButton.setBackground(Color.WHITE);
        switchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(switchButton);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputField.setText("");
                outputField.setText("");
            }
        });
        
        btnClear.setBounds(204, 78, 80, 29);
        btnClear.setBackground(Color.WHITE);
        btnClear.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(btnClear);
        

        JButton btnEquals = new JButton("=");
    btnEquals.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (binaryToHex) {
                String binary = inputField.getText();
                String hex = binaryToHexadecimal(binary);
                outputField.setText(hex.toUpperCase());
            } else {
                String hex = inputField.getText();
                String binary = hexadecimalToBinary(hex);
                outputField.setText(binary);
            }
        }
    });
        
        btnEquals.setBounds(10000, 78, 1000000, 100);
        btnEquals.setBackground(Color.WHITE);
        btnEquals.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(btnEquals);

        JButton btnToggle = new JButton("Toggle");
        
        outputField = new JTextField();
        outputField.setBounds(34, 123, 146, 560);
        frame.getContentPane().add(outputField);
        outputField.setColumns(10);

         // Add buttons for binary and hexadecimal digits
        String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "."};

        for (int i = 0; i < digits.length; i++) {
            JButton btnDigit = new JButton(digits[i]);
            btnDigit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String currentInput = inputField.getText();
                    String newDigit = e.getActionCommand();

                    // If the new digit is a decimal point and the current input already has a decimal point, ignore it
                    if (newDigit.equals(".") && currentInput.contains(".")) {
                        return;
                    }

                    // Otherwise, add the new digit to the input
                    inputField.setText(currentInput + newDigit);
                }
            });

             // Add each button to the list
             btnDigits.add(btnDigit);
             btnDigit.setBackground(Color.WHITE);
            btnDigit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

             // Initially disable buttons from '2' to 'F'
             if (binaryToHex && i >1 && i <16 ) { 
                btnDigit.setBackground(Color.WHITE);
                btnDigit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                btnDigit.setEnabled(false);
                
             }

             // Add the button to the panel
             btnDigit.setBounds(34 + (i % 4) * 60, 160 + (i / 4) * 40, 50, 29);
             frame.getContentPane().add(btnDigit);
         }

         btnToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                binaryToHex = !binaryToHex;
                if (binaryToHex) {
                    lblInput.setText("Binary Input");
                    for (int i = 2; i < btnDigits.size(); i++) {
                        btnDigits.get(i).setEnabled(false);
                    }
                } else {
                    lblInput.setText("Hexadecimal Input");
                    for (JButton btn : btnDigits) {
                        btn.setEnabled(true);
                    }
                }
                // Clear both fields
                inputField.setText("");
                outputField.setText("");
            }
        });

        btnToggle.setBounds(294, 36, 80, 29);
        btnToggle.setBackground(Color.WHITE);
        btnToggle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(btnToggle);

         // Add a ComponentListener to the JFrame to resize the buttons when the window is resized
         frame.addComponentListener(new ComponentAdapter() {
             @Override
             public void componentResized(ComponentEvent e) {
                 int width = Math.max(frame.getWidth() / 5, 60); // Set a minimum width
                 int height = Math.max(frame.getHeight() / 20, 20); // Set a minimum height
                 int textFieldHeight = Math.max(height, 26); // Manually set the text field height
                 inputField.setBounds(34, 36, width, textFieldHeight);
                 outputField.setBounds(34, 123, width, textFieldHeight);
                 btnToggle.setBounds(frame.getWidth() - 200, 36, width / 2, height); // Adjust as needed
                 btnClear.setBounds(frame.getWidth() - 200, 78, width / 2, height); // Adjust as needed
                 btnEquals.setBounds(frame.getWidth() - 400, 78, width / 2, height ); // Adjust as needed
                 switchButton.setBounds(frame.getWidth() - 200, 120, width / 2, height); // Adjust as needed
                 for (int i = 0; i < btnDigits.size(); i++) {
                     JButton btn = btnDigits.get(i);
                     btn.setBounds(34 + (i % 4) * width, 160 + (i / 4) * height * 2, width, height * 2); // Adjust as needed
                 }
             }
         });
    }

    private boolean isParsable(String input){
      boolean parsable = true;
      try{
          Integer.parseInt(input,16);
      }catch(NumberFormatException e){
          parsable = false;
      }
      return parsable;
    }

    private String binaryToHexadecimal(String binary) {
    String[] parts = binary.split("\\.");

    // Convert the integer part, if it exists
    String hexIntegerPart = "";
    if (!parts[0].isEmpty()) {
        int integerPart = Integer.parseInt(parts[0], 2);
        hexIntegerPart = Integer.toHexString(integerPart);
    }

    // Convert the fractional part, if it exists
    String hexFractionalPart = "";
    if (parts.length > 1 && !parts[1].isEmpty()) {
        double fractionalPart = Integer.parseInt(parts[1], 2) / Math.pow(2, parts[1].length());
        while (fractionalPart > 0) {
            fractionalPart *= 16;
            int digit = (int) fractionalPart;
            hexFractionalPart += Integer.toHexString(digit);
            fractionalPart -= digit;
        }
    }

    return hexIntegerPart + (hexFractionalPart.isEmpty() ? "" : "." + hexFractionalPart);
}

    private String hexadecimalToBinary(String hex) {
        String[] parts = hex.split("\\.");

        // Convert the integer part
        int integerPart = Integer.parseInt(parts[0], 16);
        String binaryIntegerPart = Integer.toBinaryString(integerPart);

        // Convert the fractional part
        String binaryFractionalPart = "";
        if (parts.length > 1) {
            for (char digit : parts[1].toCharArray()) {
                int decimalDigit = Integer.parseInt(Character.toString(digit), 16);
                binaryFractionalPart += String.format("%4s", Integer.toBinaryString(decimalDigit)).replace(' ', '0');
            }
        }

        return binaryIntegerPart + (binaryFractionalPart.isEmpty() ? "" : "." + binaryFractionalPart);
    }
}