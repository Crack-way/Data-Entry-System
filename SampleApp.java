import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class SampleApp extends JFrame {
    JButton b1, b2, b3, b4;
    JTextField t1, t2;
    JRadioButton r1, r2;
    ButtonGroup group;
    JPanel tablePanel;
    DefaultTableModel model;
    JTable table;
    JCheckBox c1;
    String gender;
    String covid;

    Connection con1;
    PreparedStatement insert;

    public SampleProject() {
        //For the frame
        setLayout(null);//Layout should be null, so we can edit the layout as we wish.
        setBounds(0,0,1020,1020);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel l1,l2,l3,l4,l5;
        JPanel p1,panel2,p3;
        JMenuBar menubar;
        JMenu m1,m2,m3;
         
        
        
        //Menu Section
        m1=new JMenu("File");
        m2=new JMenu("Edit");
        m3=new JMenu("View");
        menubar=new JMenuBar();
        menubar.add(m1);
        menubar.add(m2);
        menubar.add(m3);
        setJMenuBar(menubar);
        
        
        //Data Entry Panel section code.
        p1=new JPanel();
        p1.setBounds(0,5,500,500);
        l1=new JLabel("Data Entry");
        l1.setBounds(250,5,100,100);

        l2=new JLabel("Name");
        l2.setBounds(30,50,100,100);

        t1=new JTextField();
        t1.setBounds(80,90,100,20);

        l3=new JLabel("Address");
        l3.setBounds(30,80,100,100);

        t2=new JTextField();
        t2.setBounds(80,120,100,20);

        l4=new JLabel("Gender");
        l4.setBounds(30,115,100,100);

        r1=new JRadioButton("Male");
        r2=new JRadioButton("Female");

        group = new ButtonGroup();
        group.add(r1);
        group.add(r2);

        add(r1);
        add(r2);

        r1.setBounds(80,150,100,30);
        r2.setBounds(180,150,100,30);

        l5=new JLabel("Positive");
        l5.setBounds(30,140,100,100);
        c1=new JCheckBox();
        c1.setBounds(80,180,100,20);

        //adding the all the components in the panel
        p1.setLayout(null);
        p1.setBorder(new TitledBorder(new LineBorder(Color.blue,1),"Data Entry"));  //adding title border with the line border
        p1.add(l1);
        p1.add(l2);
        p1.add(t1);
        p1.add(l3);
        p1.add(t2);
        p1.add(l4);
        p1.add(r1);
        p1.add(r2);
        p1.add(l5);
        p1.add(c1);
        //adding panel in the frame
        add(p1);

        //Button Functionality code section
        panel2=new JPanel();
        panel2.setBounds(500,5,500,500);
        panel2.setLayout(new GridLayout(2,2));
        panel2.setBorder(new TitledBorder(new LineBorder(Color.blue,1),"Button Functionality"));

        //Button section
        b1=new JButton("Save");
        b2=new JButton("Update");
        b3=new JButton("Delete");
        b4=new JButton("Clear");

        //adding the panel2 components in the panel2
        panel2.add(b1);
        panel2.add(b2);
        panel2.add(b3);
        panel2.add(b4);

        //adding the panel 2 in the frame.
        add(panel2);


        //List of data or Table code section
        p3=new JPanel();
        p3.setBounds(0,510,1000,500);
        p3.setBorder(new TitledBorder(new LineBorder(Color.blue,1),"List of data"));

        p3.add(tableUI());
        add(p3);
        setLocationRelativeTo(null);

        //Adding event listener
        b1.addActionListener(new Handler());
        b2.addActionListener(new Handler());
        b3.addActionListener(new Handler());
        b4.addActionListener(new Handler());
    }

    private void table_update() {
        int c;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost/covid", "root", "");
            insert = con1.prepareStatement("select * from records");

            ResultSet rs = insert.executeQuery();
            ResultSetMetaData Rss = rs.getMetaData();
            c = Rss.getColumnCount();

            DefaultTableModel Df;
            Df = (DefaultTableModel) table.getModel();
            Df.setRowCount(0);

            while (rs.next()) {
                Vector v2 = new Vector();

                for (int a = 1; a <= c; a++) {
                    v2.add(rs.getString("id"));
                    v2.add(rs.getString("name"));
                    v2.add(rs.getString("address"));
                    v2.add(rs.getString("gender"));
                    v2.add(rs.getString("COVID_19"));
                }
                Df.addRow(v2);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private JPanel tableUI() {
        tablePanel = new JPanel();

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] { "ID", "Name", "Address", "Gender", "COVID-19" });
        table = new JTable(model);
        table.setBounds(0, 510, 1000, 500);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);

        // added event listener for row selection inside JTable
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // returns row number of selected row in JTable
                int selectedRow = table.getSelectedRow();
                // model.getValueAt(selectedRow, 0) returns a value for the cell at the given
                // row and column
                // .toString() returns string representation of the object
                t1.setText(model.getValueAt(selectedRow, 1).toString());
                t2.setText(model.getValueAt(selectedRow, 2).toString());
                gender = model.getValueAt(selectedRow, 3).toString();
                if (gender.equals("Male")) {
                    r1.setSelected(true);
                } else {
                    r2.setSelected(true);
                }

                covid = model.getValueAt(selectedRow, 4).toString();
                switch (covid) {
                    case "Positive":
                        c1.setSelected(true);
                        break;

                    case "Negative":
                        c1.setSelected(false);
                        break;

                    default:
                        c1.setSelected(true);
                        break;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        table_update();
        return tablePanel;
    }

    class Handler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == b1) {
                String name = t1.getText();
                String address = t2.getText();
                if (r1.isSelected()) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }

                if (c1.isSelected()) {
                    covid = "Positive";

                } else {
                    covid = "Negative";

                }

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con1 = DriverManager.getConnection("jdbc:mysql://localhost/covid", "root", "");
                    insert = con1
                            .prepareStatement("insert into records(name,address,gender,COVID_19)  	values(?,?,?,?)");
                    insert.setString(1, name);
                    insert.setString(2, address);
                    insert.setString(3, gender);
                    insert.setString(4, covid);

                    insert.executeUpdate();
                    table_update();

                    JOptionPane.showMessageDialog(b1, "Record Added", "Success", JOptionPane.INFORMATION_MESSAGE);
                    t1.setText("");
                    t2.setText("");
                    if (gender.equals("Male")) {
                        group.clearSelection();

                    } else {
                        group.clearSelection();
                    }

                    if (covid.equals("Positive")) {
                        c1.setSelected(false);
                    } else {
                        c1.setSelected(false);
                    }
                    t1.requestFocus();
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == b2) {
                DefaultTableModel Df = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();
                String name = t1.getText();
                String address = t2.getText();
                if (r1.isSelected()) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }

                if (c1.isSelected()) {
                    covid = "Positive";

                } else {
                    covid = "Negative";

                }

                try {
                    int id = Integer.parseInt(Df.getValueAt(selectedIndex, 0).toString());

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con1 = DriverManager.getConnection("jdbc:mysql://localhost/covid", "root", "");
                    insert = con1.prepareStatement(
                            "update records set name= ?, address=?, gender=?, COVID_19=? where id=? ");
                    insert.setString(1, name);
                    insert.setString(2, address);
                    insert.setString(3, gender);
                    insert.setString(4, covid);
                    insert.setInt(5, id);

                    insert.executeUpdate();
                    table_update();

                    JOptionPane.showMessageDialog(b2, "Record Updated");

                    t1.setText("");
                    t2.setText("");
                    if (gender.equals("Male")) {
                        group.clearSelection();
                    } else {
                        group.clearSelection();
                    }
                    if (covid.equals("Positive")) {
                        c1.setSelected(false);
                    } else {
                        c1.setSelected(false);
                    }
                    t1.requestFocus();
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == b3) {
                DefaultTableModel Df = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                try {
                    int id = Integer.parseInt(Df.getValueAt(selectedIndex, 0).toString());
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to Delete the Record?",
                            "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con1 = DriverManager.getConnection("jdbc:mysql://localhost/covid", "root", "");
                        insert = con1.prepareStatement("delete from records where id=?");
                        insert.setInt(1, id);

                        insert.executeUpdate();
                        table_update();

                        JOptionPane.showMessageDialog(b3, "Record Deleted");

                        t1.setText("");
                        t2.setText("");
                        if (gender.equals("Male")) {
                            group.clearSelection();
                        } else {
                            group.clearSelection();
                        }
                        if (covid.equals("Positive")) {
                            c1.setSelected(false);
                        } else {
                            c1.setSelected(false);
                        }
                        t1.requestFocus();
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }

            }
            if (e.getSource() == b4) {
                t1.setText("");
                t2.setText("");
                group.clearSelection();
                c1.setSelected(false);
                t1.setFocusable(true);
            }
        }
    }

    public static void main(String[] args) {
        SampleProject frame = new SampleProject();
        frame.setVisible(true);
    }
}
