import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Calculator {
    private JFrame frame;
    private JTextField textField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Calculator window = new Calculator();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Calculator() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(new Color(11, 91, 91));
    
        // Use GridLayout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 10, 10)); // Added gaps between grid elements
        panel.setOpaque(false); // Make the JPanel transparent
    
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 40)); // Increase font size to 40
        textField.setPreferredSize(new Dimension(200, 100)); // Set preferred size to 200x50 pixels
        frame.getContentPane().add(textField, BorderLayout.NORTH);
        textField.setColumns(10);
    
        // Creating number buttons and operations
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if ((command.charAt(0) == '*' || command.charAt(0) == '/') && textField.getText().isEmpty()) {
                    // If the text field is empty, don't allow '*' or '/' to be entered
                    return;
                }
                if (command.charAt(0) == '=') {
                    String result = evaluate(textField.getText());
                    textField.setText(result); // Display the result
                } else if (command.charAt(0) == 'C') {
                    textField.setText(""); // Clear the text field
                } else if (command.charAt(0) == '.') {
                    String[] operands = textField.getText().split("[+\\-*/]");
                    String currentOperand = operands[operands.length - 1];
                    if (currentOperand.contains(".")) {
                        // Do nothing if the current operand already contains a decimal point
                        return;
                    }
                    textField.setText(textField.getText() + command);
                } else {
                    textField.setText(textField.getText() + command);
                }
            }
        };
    
        for (int i = 0; i < 10; i++) {
            String text = String.valueOf(i);
            CustomButton btnNewButton = new CustomButton(text);
            btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase button font size
            btnNewButton.setPreferredSize(new Dimension(40, 40)); // Set button size to 50x50 pixels
            btnNewButton.addActionListener(actionListener);
            panel.add(btnNewButton); // Add button to panel
        }

        // Add decimal point button
            CustomButton decimalButton = new CustomButton(".");
            decimalButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase button font size
            decimalButton.setPreferredSize(new Dimension(40, 40)); // Set button size to 50x50 pixels
            decimalButton.addActionListener(actionListener);
            panel.add(decimalButton);
        
        // operation buttons
        String[] operations = {"+", "-", "*", "/", "=", "C"}; 
        for (String operation : operations) {
        CustomButton btnNewButton = new CustomButton(operation);
        btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increase button font size
        btnNewButton.setPreferredSize(new Dimension(40, 40)); // Set button size to 50x50 pixels
        btnNewButton.addActionListener(actionListener);
        panel.add(btnNewButton); // Add button to panel
}

        // Add "switch" button at the bottom
        CustomButton switchButton = new CustomButton("Switch");
        switchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Dispose of the current JFrame
                frame.dispose();

                // Start the Converter program
                String[] args = {};
                Converter.main(args);
            }
        });
        
        panel.add(switchButton);

        frame.pack();
    }
    
    //operations should be fully functional. Unless Pandora did something
    public static String evaluate(String expression) {
    Stack<Double> operands = new Stack<>();
    Stack<Character> operators = new Stack<>();
    char[] arr = expression.toCharArray();
    for (int i = 0; i < arr.length; i++) {
        char c = arr[i];
        if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || arr[i-1] == '+' || arr[i-1] == '-' || arr[i-1] == '*' || arr[i-1] == '/'))) {
            int j = i;
if (arr[j] == '-' && j + 1 < arr.length && Character.isDigit(arr[j + 1])) {
    j++;
}
while (j < arr.length && (Character.isDigit(arr[j]) || arr[j] == '.')) {
    j++;
}
double operand = Double.parseDouble(expression.substring(i, j));
            i = j - 1;
            operands.push(operand);
        } else if (c == '+' || c == '-' || c == '*' || c == '/') {
            while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                evaluateOperator(operators, operands);
            }
            operators.push(c);
        }
    }
    while (!operators.isEmpty()) {
        evaluateOperator(operators, operands);
    }
    return String.format("%.10f", operands.pop()).replaceAll("\\.?0+$", "");
}

private static void evaluateOperator(Stack<Character> operators, Stack<Double> operands) {
    char operator = operators.pop();
    double operand2 = operands.pop();
    double operand1 = operands.pop();
    double result = 0;
    switch (operator) {
        case '+':
            result = operand1 + operand2;
            break;
        case '-':
            result = operand1 - operand2;
            break;
        case '*':
            result = operand1 * operand2;
            break;
        case '/':
            if (operand2 != 0) {
                result = operand1 / operand2;
            } else {
                throw new IllegalArgumentException("Uncomputable");
            }
            break;
    }
    operands.push(result);
}

private static int precedence(char operator) {
    switch (operator) {
        case '+':
        case '-':
            return 1;
        case '*':
        case '/':
            return 2;
        default:
            return -1;
    }
}

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Customize the look of the button here
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        super.paintComponent(g);
    }
}
}