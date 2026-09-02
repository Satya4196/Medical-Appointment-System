package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ModernUIComponents {

    // 1. Panel with a smooth gradient background
    public static class GradientPanel extends JPanel {
        private Color startColor = new Color(30, 41, 59); // Slate Dark
        private Color endColor = new Color(15, 23, 42);   // Slate Deeper Dark

        public GradientPanel() {
            setLayout(new BorderLayout());
        }

        public GradientPanel(Color startColor, Color endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, startColor, 0, h, endColor);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
            g2d.dispose();
        }
    }

    // 2. Centered Card Panel with rounded corners and semi-transparency (Glassmorphism effect)
    public static class RoundedCard extends JPanel {
        private int cornerRadius = 24;
        private Color bgColor = new Color(255, 255, 255, 240); // Soft solid white

        public RoundedCard() {
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(25, 30, 25, 30));
        }

        public RoundedCard(int radius, Color color) {
            this.cornerRadius = radius;
            this.bgColor = color;
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(25, 30, 25, 30));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw drop shadow
            g2d.setColor(new Color(0, 0, 0, 40));
            g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);
            
            // Draw card background
            g2d.setColor(bgColor);
            g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            
            // Draw subtle border
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
            g2d.dispose();
        }
    }

    // 3. Rounded Text Field with Active Focus Border & Placeholder text
    public static class ModernTextField extends JTextField {
        private String placeholder;
        private Color focusColor = new Color(99, 102, 241); // Indigo
        private Color borderColor = new Color(226, 232, 240); // Light gray
        private boolean isFocused = false;

        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setForeground(new Color(30, 41, 59));
            setCaretColor(focusColor);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(new EmptyBorder(8, 12, 8, 12));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Paint background
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);

            // Paint border
            g2d.setColor(isFocused ? focusColor : borderColor);
            g2d.setStroke(new BasicStroke(isFocused ? 2f : 1f));
            g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);

            g2d.dispose();
            super.paintComponent(g);

            // Paint placeholder
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D gPlaceholder = (Graphics2D) g.create();
                gPlaceholder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gPlaceholder.setColor(new Color(148, 163, 184)); // Slate placeholder color
                gPlaceholder.setFont(getFont().deriveFont(Font.ITALIC));
                FontMetrics fm = gPlaceholder.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                gPlaceholder.drawString(placeholder, 12, y);
                gPlaceholder.dispose();
            }
        }
    }

    // 4. Rounded Password Field with Active Focus Border & Placeholder text
    public static class ModernPasswordField extends JPasswordField {
        private String placeholder;
        private Color focusColor = new Color(99, 102, 241); // Indigo
        private Color borderColor = new Color(226, 232, 240); // Light gray
        private boolean isFocused = false;

        public ModernPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
            setForeground(new Color(30, 41, 59));
            setCaretColor(focusColor);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(new EmptyBorder(8, 12, 8, 12));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Paint background
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);

            // Paint border
            g2d.setColor(isFocused ? focusColor : borderColor);
            g2d.setStroke(new BasicStroke(isFocused ? 2f : 1f));
            g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);

            g2d.dispose();
            super.paintComponent(g);

            // Paint placeholder
            if (getPassword().length == 0 && !isFocusOwner()) {
                Graphics2D gPlaceholder = (Graphics2D) g.create();
                gPlaceholder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                gPlaceholder.setColor(new Color(148, 163, 184));
                gPlaceholder.setFont(getFont().deriveFont(Font.ITALIC));
                FontMetrics fm = gPlaceholder.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                gPlaceholder.drawString(placeholder, 12, y);
                gPlaceholder.dispose();
            }
        }
    }

    // 5. Rounded Flat Button with Hover Animations
    public static class ModernButton extends JButton {
        private Color baseColor = new Color(99, 102, 241); // Indigo Primary
        private Color hoverColor = new Color(79, 70, 229); // Darker Indigo
        private Color clickColor = new Color(67, 56, 202); // Deep Indigo
        private Color currentColor;

        public ModernButton(String text) {
            super(text);
            currentColor = baseColor;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    currentColor = hoverColor;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    currentColor = baseColor;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    currentColor = clickColor;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    currentColor = hoverColor;
                    repaint();
                }
            });
        }

        // Overloaded constructor for secondary/outline buttons
        public ModernButton(String text, boolean isSecondary) {
            this(text);
            if (isSecondary) {
                baseColor = new Color(248, 250, 252); // Soft light slate
                hoverColor = new Color(241, 245, 249); // Slate hover
                clickColor = new Color(226, 232, 240);
                currentColor = baseColor;
                setForeground(new Color(71, 85, 105)); // Slate text
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(currentColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            // Draw a subtle border for secondary button
            if (getForeground().equals(new Color(71, 85, 105))) {
                g2d.setColor(new Color(203, 213, 225));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // 6. Styled Sidebar Tab Buttons
    public static class SidebarButton extends JButton {
        private boolean active = false;
        private Color hoverBg = new Color(255, 255, 255, 20);
        private Color activeBg = new Color(255, 255, 255, 35);
        
        public SidebarButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(new Color(241, 245, 249)); // light slate
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(12, 20, 12, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }
        
        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Point mousePoint = getMousePosition();
            boolean isHover = mousePoint != null;
            
            if (active) {
                g2d.setColor(activeBg);
                g2d.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 8, 8);
                // Highlight Indigo indicator line
                g2d.setColor(new Color(99, 102, 241));
                g2d.fillRoundRect(12, 10, 4, getHeight() - 20, 2, 2);
            } else if (isHover) {
                g2d.setColor(hoverBg);
                g2d.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 8, 8);
            }
            
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // 7. Stat Widget Cards
    public static class StatCard extends JPanel {
        public StatCard(String title, String value, Color accentColor) {
            setOpaque(false);
            setLayout(new BorderLayout(8, 8));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            
            JLabel valLabel = new JLabel(value);
            valLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
            valLabel.setForeground(new Color(30, 41, 59));
            
            JLabel titleLabel = new JLabel(title.toUpperCase());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
            titleLabel.setForeground(new Color(148, 163, 184));
            
            JPanel bar = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    g.setColor(accentColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            bar.setPreferredSize(new Dimension(4, 0));
            
            JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
            textPanel.setOpaque(false);
            textPanel.add(valLabel);
            textPanel.add(titleLabel);
            
            add(bar, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            g2d.setColor(new Color(241, 245, 249));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            g2d.dispose();
        }
    }

    // 8. Custom JTable styling utility
    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setGridColor(new Color(241, 245, 249)); // light border
        table.setSelectionBackground(new Color(238, 242, 255)); // light indigo selection
        table.setSelectionForeground(new Color(79, 70, 229)); // indigo text selection
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        
        // Header styling
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(99, 102, 241));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(100, 36));
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        
        table.setBorder(BorderFactory.createEmptyBorder());
    }
}
