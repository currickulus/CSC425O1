import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MultiDance extends JFrame {

    private JTextField[] numberBoxes = new JTextField[5];
    private JLabel productLabel;
    private JTextArea danceZone;

    public MultiDance() {
        startUp();
    }
//this creates the window
    private void startUp() {
        setTitle("Multi Dance");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//These are the directions for the dancer output text box
        JPanel dancerPanel = new JPanel(new BorderLayout());
        danceZone = new JTextArea(5, 10);
        danceZone.setEditable(false);
        danceZone.setFont(new Font("Dancer", Font.PLAIN, 12));
        dancerPanel.add(new JScrollPane(danceZone), BorderLayout.CENTER);
//these are the directions for the creation of the calculation area of the window
        JPanel calculationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        addNumberFieldsRecursively(calculationPanel, gbc, 0);

        JButton calculateButton = new JButton("Calculate Product");
        calculateButton.addActionListener(e -> calculateProduct());
        calculationPanel.add(calculateButton, gbc);

        productLabel = new JLabel("Product: ");
        calculationPanel.add(productLabel, gbc);

        add(dancerPanel, BorderLayout.NORTH);
        add(calculationPanel, BorderLayout.CENTER);

        // Start the dancer
        new Thread(this::danceLoop).start();
    }
//gets the numbers to create a product
    private void addNumberFieldsRecursively(JPanel panel, GridBagConstraints gbc, int index) {
        if (index < 5) {
            panel.add(new JLabel(index == 0 ? "Enter numbers: " : ""), gbc);
            numberBoxes[index] = new JTextField(5);
            panel.add(numberBoxes[index], gbc);
            addNumberFieldsRecursively(panel, gbc, index + 1);
        }
    }
//this creates the array of numbers included in the product and handles exceptions
    private void calculateProduct() {//pushing the button is the base case
        ArrayList<Integer> numbers = new ArrayList<>();
        for (JTextField field : numberBoxes) {
            String text = field.getText().trim();
            if (!text.isEmpty()) {
                try {
                    numbers.add(Integer.parseInt(text));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Failed to enter a number.");
                    return; // Failed to enter a number
                }
            }
        }//This is the exception if no numbers are entered when you push calculate
        if (numbers.isEmpty()) {
            productLabel.setText("Product: No numbers entered");
        } else {//this prints the product from the recursiveProduct method
            int result = recursiveProduct(numbers, 0);
            productLabel.setText("Product: " + result);
        }
    }

    // This is setup so that even if you dont enter all five numbers
    // it will caclulate what you have.
    private int recursiveProduct(ArrayList<Integer> numbers, int index) {
        if (index >= numbers.size()) {
            return 1; // Base case: all numbers processed
        }//this actually multiplies the numbers
        return numbers.get(index) * recursiveProduct(numbers, index + 1);
    }
//It took a while to figure out how to get the dancer to dance dancer array
    private String[] dancerFrames = {
            "  O  \n /| \n / \\ ",
            "  O\n  |\\  \n  | \\  ",
            "  O  \n /|\\ \n /|  "
    };
    private int frameIndex = 0;

    // this is the hold time and initialization of the dance frame
    private void danceLoop() {
        dance();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.isVisible()) {
            SwingUtilities.invokeLater(this::danceLoop);
        }
    }
//recursion to advance the dancer loop
    private void dance() {
        SwingUtilities.invokeLater(() -> {
            danceZone.setText(dancerFrames[frameIndex]);
            frameIndex = (frameIndex + 1) % dancerFrames.length;
        });
    }
//this sends the dancer loop back to the beginning
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MultiDance app = new MultiDance();
            app.setVisible(true);
        });
    }
}