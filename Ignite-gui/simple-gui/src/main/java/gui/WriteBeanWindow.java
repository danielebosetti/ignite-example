package gui;

import java.awt.EventQueue;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import controller.ControllerFactory;
import controller.IgniteController;
import util.Util;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class WriteBeanWindow {

  private JFrame frame;
  private JTextField txtPmsCode;
  private JTextField txtMydesc;
  
  private IgniteController controller;
  private static Frame frame_;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    WriteBeanWindow window =null;
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          WriteBeanWindow window = new WriteBeanWindow();
          window.frame.setVisible(true);
          setFrame(window.frame);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    
    Util.pressKey();
    frame_.dispatchEvent(new WindowEvent(frame_, WindowEvent.WINDOW_CLOSING));
  }
  
  static void setFrame(Frame frame) {
    frame_ = frame;
  }

  /**
   * Create the application.
   */
  public WriteBeanWindow() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    
    controller = ControllerFactory.newController();
    
    frame = new JFrame();
    frame.setBounds(100, 100, 450, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    JButton btnWriteProduct = new JButton("write product");
    btnWriteProduct.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String pmsCode = txtPmsCode.getText();
        String desc = txtMydesc.getText();
        controller.writeData(pmsCode, desc);
      }
    });
    btnWriteProduct.setBounds(39, 65, 122, 23);
    frame.getContentPane().add(btnWriteProduct);
    
    txtPmsCode = new JTextField();
    txtPmsCode.setText("pmsCode0");
    txtPmsCode.setBounds(182, 66, 86, 20);
    frame.getContentPane().add(txtPmsCode);
    txtPmsCode.setColumns(10);
    
    txtMydesc = new JTextField();
    txtMydesc.setText("myDesc1");
    txtMydesc.setBounds(278, 66, 86, 20);
    frame.getContentPane().add(txtMydesc);
    txtMydesc.setColumns(10);
    
    JLabel lblPmscode = new JLabel("pms-code");
    lblPmscode.setBounds(182, 45, 46, 14);
    frame.getContentPane().add(lblPmscode);
    
    JLabel lblDesc = new JLabel("desc");
    lblDesc.setBounds(278, 45, 46, 14);
    frame.getContentPane().add(lblDesc);
    
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent winEvt) {
        controller.close();
      }
    });
  }
}
