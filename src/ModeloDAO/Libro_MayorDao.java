/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;


import  Modelo.libro_mayor;
import Modelo.ConexiónBD;
import com.toedter.calendar.JDateChooser;
import vista_Libro_Contable.frm_LibroMayor;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Adrian
 */
public class Libro_MayorDao  extends  ConexiónBD{
    
    
     private ResultSet rs;
    private PreparedStatement ps;
    
    
    libro_mayor Lm = new libro_mayor();
    
    
     DefaultTableModel modeloLibroMayor = new DefaultTableModel();
    
    
    
      
      public void TituloLibroMayor() {
    String Titulo[] = {
        "ID", 
        "Cuenta", 
        "Fecha",
        "Saldo Anterior",
        "Debe", 
        "Haber",
        "Saldo Final $"
    };

    modeloLibroMayor.setColumnIdentifiers(Titulo);

    if (frm_LibroMayor.TablaLibroMayor != null) {
      frm_LibroMayor.TablaLibroMayor.setModel(modeloLibroMayor);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(frm_LibroMayor.TablaLibroMayor);
    }
}

      
      
    
    
    private void limpiarTablaLibroMayor (){
 int fila = modeloLibroMayor.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloLibroMayor.removeRow(0);
        }
   
    }
    
}
    
    
    public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER); // mejor usar SwingConstants

    // Recorremos todas las columnas visibles
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}
    
    
 private ArrayList<libro_mayor> ListaLibroMayor() throws ClassNotFoundException, SQLException {
    ArrayList<libro_mayor> Lista = new ArrayList<>();
    
    try {
        String sql = "Select " +
                "l.id_libro_mayor, " +
                "c.nombre, " +
                "l.fecha, " +
                "l.saldo_anterior, " +
                "l.debe, " +
                "l.haber, " +
                "l.saldo_final " +  // Asegúrate que esta columna existe en tu BD
                "From libro_mayor l " +
                "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                "ORDER BY l.fecha, l.id_libro_mayor";  // Ordenar por fecha
        
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            libro_mayor LM = new libro_mayor();
            
            LM.setId_Libro_mayor(rs.getInt("id_libro_mayor"));
            LM.setId_Cuenta(rs.getString("nombre"));
            LM.setFecha(rs.getString("fecha"));
            LM.setSaldo_anterior(rs.getDouble("saldo_anterior"));
            LM.setDebe(rs.getDouble("debe"));
            LM.setHaber(rs.getDouble("haber"));
            LM.setSaldo_Final(rs.getDouble("saldo_final"));
            
            Lista.add(LM);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en Obtener la lista del Libro Mayor: " + e.getMessage());
        this.cerrarCn();
    }
    
    return Lista;
}
    
    
    
    public  void MostrarLibro_Mayor () throws ClassNotFoundException, SQLException{
        
    TituloLibroMayor();
    limpiarTablaLibroMayor();
    
    ArrayList<libro_mayor> ListaTerminada =ListaLibroMayor();
    
    modeloLibroMayor = (DefaultTableModel) frm_LibroMayor.TablaLibroMayor.getModel();
    
    Object [] obj = new Object[7];
    
        for (int i = 0; i < ListaTerminada.size(); i++) {
            
           obj [0] = ListaTerminada.get(i).getId_Libro_mayor();
           obj [1] = ListaTerminada.get(i).getId_Cuenta();
           obj [2] = ListaTerminada.get(i).getFecha();
           obj [3] = ListaTerminada.get(i).getSaldo_anterior();
           obj [4] = ListaTerminada.get(i).getDebe();
           obj [5] = ListaTerminada.get(i).getHaber();
           obj [6] = ListaTerminada.get(i).getSaldo_Final();
           
           modeloLibroMayor.addRow(obj);
     
        }
    
        
        
        frm_LibroMayor.TablaLibroMayor.setModel(modeloLibroMayor);
        centrarTextoTabla(frm_LibroMayor.TablaLibroMayor);
        
        
 
        
    }
    
    
    
    
    
 public void FiltrarLibroMayorPorFecha(JTable tabla, JDateChooser desde, JDateChooser hasta) throws SQLException {
    java.util.Date fechaDesdeUtil = desde.getDate();
    java.util.Date fechaHastaUtil = hasta.getDate();

    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); // Limpiar siempre

    try {
        this.conectar();

        // CASO 1: Ambas fechas vacías → CARGAR TODO
        if (fechaDesdeUtil == null && fechaHastaUtil == null) {
            String sqlTodo = "SELECT " +
                             "l.id_libro_mayor, " +
                             "c.nombre, " +
                             "l.fecha, " +
                             "l.saldo_anterior, " +
                             "l.debe, " +
                             "l.haber, " +
                             "l.saldo_final " +
                             "FROM libro_mayor l " +
                             "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                             "ORDER BY l.fecha, l.id_libro_mayor";

            ps = this.con.prepareStatement(sqlTodo);
            rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_libro_mayor"),
                    rs.getString("nombre"),
                    rs.getDate("fecha"),
                    rs.getDouble("saldo_anterior"),
                    rs.getDouble("debe"),
                    rs.getDouble("haber"),
                    rs.getDouble("saldo_final")
                });
            }
            return;
        }

        // CASO 2: Validar ambas fechas
        if (fechaDesdeUtil == null || fechaHastaUtil == null) {
            JOptionPane.showMessageDialog(null, 
                "Por favor selecciona ambas fechas para filtrar.", 
                "Fechas incompletas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.sql.Date fechaDesde = new java.sql.Date(fechaDesdeUtil.getTime());
        java.sql.Date fechaHasta = new java.sql.Date(fechaHastaUtil.getTime());

        if (fechaDesde.after(fechaHasta)) {
            JOptionPane.showMessageDialog(null, 
                "La fecha 'Desde' no puede ser mayor que 'Hasta'.", 
                "Rango inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // CASO 3: FILTRAR POR RANGO
        String sqlFiltro = "SELECT " +
                           "l.id_libro_mayor, " +
                           "c.nombre, " +
                           "l.fecha, " +
                           "l.saldo_anterior, " +
                           "l.debe, " +
                           "l.haber, " +
                           "l.saldo_final " +
                           "FROM libro_mayor l " +
                           "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                           "WHERE l.fecha BETWEEN ? AND ? " +
                           "ORDER BY l.fecha, l.id_libro_mayor";

        ps = this.con.prepareStatement(sqlFiltro);
        ps.setDate(1, fechaDesde);
        ps.setDate(2, fechaHasta);
        rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id_libro_mayor"),
                rs.getString("nombre"),
                rs.getDate("fecha"),
                rs.getDouble("saldo_anterior"),
                rs.getDouble("debe"),
                rs.getDouble("haber"),
                rs.getDouble("saldo_final")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error al filtrar Libro Mayor: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        this.cerrarCn();
    }
}
    

    
    
    
}
