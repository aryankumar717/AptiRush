import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class AptiRush extends JFrame {

    JLabel questionLabel = new JLabel("Question");
    JButton btnA = new JButton("A");
    JButton btnB = new JButton("B");
    JButton btnC = new JButton("C");
    JButton btnD = new JButton("D");
    JLabel levelLabel = new JLabel("Level: EASY");
    JLabel scoreLabel = new JLabel("Score: 0");

    class QA {
        String q, a, b, c, d, correct;
        QA(String q, String a, String b, String c, String d, String cor) {
            this.q = q; this.a = a; this.b = b; this.c = c; this.d = d; this.correct = cor;
        }
    }

    ArrayList<QA> questions = new ArrayList<>();
    int index = 0;
    int score = 0;
    String username;

    int level = 1;

    public AptiRush() {
        setTitle("AptiRush - Aptitude Game");
        setSize(650, 450);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(levelLabel);
        top.add(scoreLabel);
        add(top, BorderLayout.NORTH);

        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(questionLabel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 10, 10));
        buttons.add(btnA);
        buttons.add(btnB);
        buttons.add(btnC);
        buttons.add(btnD);
        add(buttons, BorderLayout.SOUTH);

        btnA.addActionListener(e -> checkAnswer("A"));
        btnB.addActionListener(e -> checkAnswer("B"));
        btnC.addActionListener(e -> checkAnswer("C"));
        btnD.addActionListener(e -> checkAnswer("D"));

        askUsername();
        loadLevel();
        updateQuestion();

        setVisible(true);
    }

    void askUsername() {
        username = JOptionPane.showInputDialog(this, "Enter your name:");
        if (username == null || username.isEmpty()) username = "Player";
    }

    void loadLevel() {
        questions.clear();
        index = 0;

        String file = switch (level) {
            case 1 -> "easy.txt";
            case 2 -> "medium.txt";
            default -> "hard.txt";
        };

        levelLabel.setText("Level: " + (level == 1 ? "EASY" : level == 2 ? "MEDIUM" : "HARD"));

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String q, a, b, c, d, cor;
            while (true) {
                q = br.readLine();
                if (q == null) break;
                a = br.readLine();
                b = br.readLine();
                c = br.readLine();
                d = br.readLine();
                cor = br.readLine();

                questions.add(new QA(q, a, b, c, d, cor));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading file!");
        }

        Collections.shuffle(questions);
    }

    void updateQuestion() {
        if (index >= questions.size()) {
            finishLevel();
            return;
        }

        QA q = questions.get(index);
        questionLabel.setText("<html>" + q.q + "</html>");
        btnA.setText(q.a);
        btnB.setText(q.b);
        btnC.setText(q.c);
        btnD.setText(q.d);
    }

    void checkAnswer(String opt) {
        QA q = questions.get(index);

        if (opt.equals(q.correct)) {
            score++;
            scoreLabel.setText("Score: " + score);
        }

        index++;
        updateQuestion();
    }

    void finishLevel() {
        JOptionPane.showMessageDialog(this,
                "Level Complete!\nScore: " + score);

        // If failed and not last level
        if (score < 3 && level != 3) {
            saveScore();
            showLeaderboard();
            JOptionPane.showMessageDialog(this, "Score too low. Game over!");
            System.exit(0);
        }

        // Ask if user wants next level
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to move to the next level?",
                "Next Level?",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.NO_OPTION) {
            saveScore();
            showLeaderboard();
            JOptionPane.showMessageDialog(this, "Final Score: " + score);
            System.exit(0);
        }

        level++;

        // Finished all levels?
        if (level > 3) {
            saveScore();
            showLeaderboard();
            JOptionPane.showMessageDialog(this, "Game Finished! Final Score: " + score);
            System.exit(0);
        }

        loadLevel();
        updateQuestion();
    }

    void showLeaderboard() {
        java.util.List<String> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("scores.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                entries.add(line);
            }
        } catch (Exception e) {}

        // Sort by score descending
        entries.sort((a, b) -> {
            try {
                int s1 = Integer.parseInt(a.split(" scored ")[1].split(" ")[0]);
                int s2 = Integer.parseInt(b.split(" scored ")[1].split(" ")[0]);
                return Integer.compare(s2, s1);
            } catch (Exception e) { return 0; }
        });

        StringBuilder msg = new StringBuilder("=== Leaderboard (Top 5) ===\n\n");
        int limit = Math.min(5, entries.size());
        for (int i = 0; i < limit; i++) {
            msg.append((i + 1) + ". " + entries.get(i) + "\n");
        }
        JOptionPane.showMessageDialog(this, msg.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    void saveScore() {
        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(username + " scored " + score + " on " + LocalDateTime.now() + "\n");
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        new AptiRush();
    }
}