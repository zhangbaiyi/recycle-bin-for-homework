package view;

import helper.Coordinate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Screen {
    /**
     * {
     * 创建并显示GUI。出于线程安全的考虑，
     * 这个方法在事件调用线程中调用。
     */
    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(false);

        JFrame frame = new JFrame("1952060-张佰一-IoT期中项目");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new BorderLayout(10, 10));

        JPanel ctrlPanel = new JPanel();
        ctrlPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        ctrlPanel.setLayout(new BorderLayout(0, 0));

        frame.setContentPane(contentPanel);

        Checkbox[] checkboxes = new Checkbox[96];
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i] = new Checkbox();
        }

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(8, 12, 10, 10));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 12; j++) {
                checkboxes[i * 12 + j].setCol(j);
                checkboxes[i * 12 + j].setRow(i);
                checkboxes[i * 12 + j].ckbox.setText("(" + i + "," + j + ")");
                checkBoxPanel.add(checkboxes[i * 12 + j].ckbox);
            }
        }

        ctrlPanel.add(checkBoxPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoPanel.setLayout(new BorderLayout(0, 0));

        JPanel routeArea = new Grid();
        routeArea.setSize(190, 420);
        routeArea.setVisible(true);
        infoPanel.add(routeArea, BorderLayout.CENTER);

        JTextArea specsArea = new JTextArea("Specs of this process is shown here.", 10, 25);
        specsArea.setEditable(false);
        specsArea.setLineWrap(true);
        specsArea.setForeground(Color.black);
        specsArea.setFont(new Font("Arial", Font.BOLD, 16));
        specsArea.setBackground(Color.lightGray);
        JScrollPane specsAreaScrollPane = new JScrollPane(specsArea);
        infoPanel.add(specsAreaScrollPane, BorderLayout.NORTH);

        JPanel sliderPanel = new JPanel();
        sliderPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        sliderPanel.setLayout(new BorderLayout(10, 10));

        JLabel neiReadOnlyLabel = new JLabel("邻居距离");
        JLabel neiValueLabel = new JLabel();
        JSlider neiDistanceSlider = new JSlider(200, 500);
        neiDistanceSlider.setMinimum(1);
        neiDistanceSlider.setMajorTickSpacing(50);
        neiDistanceSlider.setPaintLabels(true);
        neiDistanceSlider.setPaintTicks(true);
        neiDistanceSlider.addChangeListener(e -> neiValueLabel.setText("当前值：" + neiDistanceSlider.getValue()));

        JLabel speedValueLabel = new JLabel();
        JSlider speedSlider = new JSlider(200, 500);
        speedSlider.setMinimum(1);
        speedSlider.setMajorTickSpacing(50);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(e -> speedValueLabel.setText("当前值：" + speedSlider.getValue()));


        JPanel neiSliderPanel = new JPanel();
        neiSliderPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        neiSliderPanel.setLayout(new BorderLayout(0, 0));
        neiSliderPanel.add(neiReadOnlyLabel, BorderLayout.WEST);
        neiSliderPanel.add(neiDistanceSlider, BorderLayout.CENTER);
        neiSliderPanel.add(neiValueLabel, BorderLayout.EAST);

        JLabel speedReadOnlyLabel = new JLabel("传输速度");


        JPanel speedSliderPanel = new JPanel();
        speedSliderPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        speedSliderPanel.setLayout(new BorderLayout(0, 0));
        speedSliderPanel.add(speedReadOnlyLabel, BorderLayout.WEST);
        speedSliderPanel.add(speedSlider, BorderLayout.CENTER);
        speedSliderPanel.add(speedValueLabel, BorderLayout.EAST);

        sliderPanel.add(neiSliderPanel, BorderLayout.NORTH);
        sliderPanel.add(speedSliderPanel, BorderLayout.SOUTH);

        List<Coordinate> selectedCoordinates = new ArrayList<>();

        JButton confirmButton = new JButton("START");
        confirmButton.addActionListener(e -> {
            neiDistanceSlider.setEnabled(false);
            speedSlider.setEnabled(false);
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].ckbox.isSelected()) {
                    Coordinate curCoordinate = new Coordinate(i / 12, i % 12);
                    selectedCoordinates.add(curCoordinate);
                }
                checkboxes[i].ckbox.setEnabled(false);
            }

            if (selectedCoordinates.size() < 3) {
                JOptionPane.showConfirmDialog(frame, "抱歉，所选结点数不能少于三个", "您选择的节点数非法", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                frame.dispose();
            } else {
                long delayTime = (Double.valueOf((1.0 * neiDistanceSlider.getValue()) / (1.0 * speedSlider.getValue()))).longValue();
                new route.Routing(selectedCoordinates, delayTime).startRouting(routeArea.getGraphics(), specsArea);
            }

        });

        ctrlPanel.add(confirmButton, BorderLayout.SOUTH);
        ctrlPanel.add(sliderPanel, BorderLayout.NORTH);

        contentPanel.add(ctrlPanel, BorderLayout.WEST);
        contentPanel.add(infoPanel, BorderLayout.EAST);

        // 显示窗口
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 显示应用 GUI
        SwingUtilities.invokeLater(Screen::createAndShowGUI);
    }
}