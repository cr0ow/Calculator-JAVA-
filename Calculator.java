import org.w3c.dom.css.RGBColor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends Thread {
    JFrame frame = new JFrame("Calculator");
    ImageIcon img = new ImageIcon("calculator.jar\\icon.png");
    JTextField textField = new JTextField();
    JButton[] numbers = new JButton[10];
    JButton[] specials = new JButton[9];
    double[] number = {0, 0};
    boolean[] filled = {false, false};
    char mark = ';';

    private void createWindow() {
        frame.setSize(new Dimension(338, 494));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(img.getImage());

        textField.setSize(new Dimension(frame.getWidth() - 30, 40));
        textField.setLocation(new Point(7, 10));
        textField.setEditable(false);
        textField.setText("0");
        textField.setBackground(Color.WHITE);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setMargin(new Insets(0, 3, 0, 3));
        textField.setFont(new Font("Monospace", Font.BOLD, 20));
        frame.add(textField);
    }

    public void run() {
        createWindow();
        createButtons();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.start();
    }

    class NumberListener implements ActionListener {
        String key;

        public NumberListener(String key) {
            this.key = key;
        }

        private boolean isNumber(String text) {
            try {
                Double.parseDouble(text);
            }
            catch (NumberFormatException ex) {
                return false;
            }
            return true;
        }

        private void count(String text) {
            if(mark != ';') {
                number[1] = Double.parseDouble(text);
                filled[1] = true;
                switch (mark) {
                    case '+' -> textField.setText(Double.toString(number[0] + number[1]));
                    case '-' -> textField.setText(Double.toString(number[0] - number[1]));
                    case 'x' -> textField.setText(Double.toString(number[0] * number[1]));
                    case '/' -> {
                        if(number[1] == 0) {
                            textField.setText("Divide by 0!");
                            number[0] = 0;
                            number[1] = 0;
                            filled[0] = false;
                            filled[1] = false;
                            mark = ';';
                            return;
                        }
                        textField.setText(Double.toString(number[0] / number[1]));
                    }
                }
                number[0] = Double.parseDouble(textField.getText());
                number[1] = 0;
                filled[0] = true;
                filled[1] = false;
                mark = ';';
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textField.getText();
            boolean actionTaken = false;

            switch (key) {
                case "+", "-", "x", "/" -> {
                    if (isNumber(text)) {
                        number[0] = Double.parseDouble(text);
                        filled[0] = true;
                        mark = key.charAt(0);
                        textField.setText(key);
                    }
                    actionTaken = true;
                }
                case "=" -> {
                    if (isNumber(text))
                        count(text);
                    actionTaken = true;
                }
                case "C" -> {
                    if (isNumber(text)) {
                        text = text.substring(0, text.length() - 1);
                        if (!isNumber(text))
                            text = "0";
                        textField.setText(text);
                    }
                    actionTaken = true;
                }
                case "CE" -> {
                    if (isNumber(text)) {
                        textField.setText("0");
                        number[0] = 0;
                        number[1] = 0;
                        filled[0] = false;
                        filled[1] = false;
                    }
                    actionTaken = true;
                }
                case "+/-" -> {
                    if (isNumber(text)) {
                        if (text.charAt(0) == '-')
                            text = text.substring(1);
                        else if (!text.equals("0"))
                            text = "-" + text;
                        textField.setText(text);
                    }
                    actionTaken = true;
                }
                case "." -> {
                    if (isNumber(text)) {
                        boolean hasDot = false;
                        for (int i = 0; i < text.length(); i++)
                            if (text.charAt(i) == '.') {
                                hasDot = true;
                                break;
                            }
                        if (!hasDot)
                            textField.setText(text + ".");
                    }
                    actionTaken = true;
                }
            }

            if(!actionTaken) {
                if (key.equals("0") && text.equals("0"))
                    return;
                if (text.equals("0") || !isNumber(text))
                    text = "";
                textField.setText(text + key);
            }
        }
    }

    private void createButtons() {
        int num = 0;
        JButton button = newButton(Integer.toString(num), 7 + 70 + 10, 70 + 60 + 70 * 3 + 30 + 10);
        numbers[num] = button;
        num++;
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                button = newButton(Integer.toString(num), 7 + 70 * (j % 3) + 10 * (j % 3), 70 + 60 + 70 * (i % 3) + 10 * (i % 3) + 10);
                numbers[num] = button;
                num++;
            }
        }
        button = newButton("+", 7 + 70 * 3 + 10 * 3, 70 + 60 + 10);
        specials[0] = button;
        button = newButton("-", 7 + 70 * 3 + 10 * 3, 70 + 60 + 70 + 10 + 10);
        specials[1] = button;
        button = newButton("x", 7 + 70 * 3 + 10 * 3, 70 + 60 + 70 * 2 + 10 * 2 + 10);
        specials[2] = button;
        button = newButton("/", 7 + 70 * 3 + 10 * 3, 70 + 60 + 70 * 3 + 10 * 3 + 10);
        specials[3] = button;
        button = newButton("=", 7 + 70 * 2 + 10 * 2, 70 + 60 + 70 * 3 + 10 * 3 + 10);
        button.setBackground(Color.ORANGE);
        specials[4] = button;
        button = newButton(".", 7, 70 + 60 + 70 * 3 + 10 * 3 + 10);
        specials[5] = button;
        button = newButton("CE", 7, 60);
        specials[6] = button;
        button = newButton("C", 7 + 70 + 10, 60);
        specials[7] = button;
        button = newButton("+/-", 7 + 70 * 2 + 10 * 2, 60);
        specials[8] = button;
    }

    private JButton newButton(String value, int x, int y) {
        JButton button = new JButton("<html><font color='white'>" + value + "</font></html>");
        button.setBackground(new Color(46, 44, 44));
        button.setSize(new Dimension(70, 70));
        button.setFont(new Font("Monospace", Font.BOLD, 20));
        button.setLocation(x, y);
        button.addActionListener(new NumberListener(value));
        frame.add(button);
        return button;
    }

}
