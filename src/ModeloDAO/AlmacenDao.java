
package ModeloDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import Vista_Almacen.Gestionar_Almacenn;
import Modelo.Almacen;
import Modelo.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;

import Vista_Gestion_Categoria.frm_Categoria;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import Vista_GestionInventario.InventarioLiquido;
import Vista_GestionInventario.InventarioSolido;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import Vista_GestionInventario.InventarioUnidad;

public class AlmacenDao  extends  ConexiónBD{
    
    PreparedStatement ps;
     ResultSet rs;
    

    Almacen A = new Almacen();

Controlador.controladorVisual ct = new  Controlador.controladorVisual();

    
    
    DefaultTableModel modeloAlmacen = new DefaultTableModel();
    

    
    
    
    
    
    // Metodo para los titulos de la tabla empleado con el modelo de la tabla
     public void TituloAlmacen(){
   
    String Titulo [] = { "ID_Ubicación","PASILLO"," ALA  ", " ESTANTE ","NIVEL"," CAPACIDAD " ," PRODUCTO ", " STOCK ACTUAL " };
    modeloAlmacen.setColumnIdentifiers(Titulo);
  Gestionar_Almacenn.TablaAlmacen.setModel(modeloAlmacen);
 
    
}

     // Metodo para centralizar los datos de la tabla empleado
 public  void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
 
 // Metodo para limpiar la tabla  empleado 
private void limpiarTablaAlmacen (){
 int fila = modeloAlmacen.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloAlmacen.removeRow(0);
        }
        
    }
    
}

    
    
    
    
    
    
    
    
    
    
    
       


public  void RegistrarAlmacen () throws ClassNotFoundException,SQLException {
    
   
    
    
    A.setPasillo(Gestionar_Almacenn.txtPasillolmacen.getText());
    
     A.setAla((String) Gestionar_Almacenn.ComboxAla.getSelectedItem());
    
    A.setEstante(Gestionar_Almacenn.txtEstanteAlmacen.getText());
    
    A.setNivel(Integer.parseInt(Gestionar_Almacenn.txtNivel.getText()));
    
    A.setCapacidad(Integer.parseInt(Gestionar_Almacenn.txtCapacidadAlmacen.getText()));
    
    String sql = "INSERT INTO almacen (pasillo,ala,estante,nivel,capacidad) values (?,?,?,?,?)";
    
    try {
        
        this.conectar();
        
        ps = this.con.prepareStatement(sql);
        
        ps.setString(1, A.getPasillo());
        ps.setString(2, A.getAla());
        ps.setString(3, A.getEstante());
        ps.setInt(4, A.getNivel());
        ps.setInt(5, A.getCapacidad());
        
        if (ps.executeUpdate() > 0) {
            
            JOptionPane.showMessageDialog(null,"Registro exitoso de almacen");
            MostrarAlmacen();
             Gestionar_Almacenn.txtCapacidadAlmacen.setText("");
             Gestionar_Almacenn.txtEstanteAlmacen.setText("");
             Gestionar_Almacenn.txtNivel.setText("");
             Gestionar_Almacenn.txtPasillolmacen.setText(" ");
            
        }
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al registrar almacen" + e.getMessage());
    }
    
                       


}



public  void ActualizarAlmacen () throws  ClassNotFoundException,SQLException {
    
       
    A.setPasillo(Gestionar_Almacenn.txtPasillolmacen.getText());
    
     A.setAla((String) Gestionar_Almacenn.ComboxAla.getSelectedItem());
    
    A.setEstante(Gestionar_Almacenn.txtEstanteAlmacen.getText());
    
    A.setNivel(Integer.parseInt(Gestionar_Almacenn.txtNivel.getText()));
    
    A.setCapacidad(Integer.parseInt(Gestionar_Almacenn.txtCapacidadAlmacen.getText()));
    
    A.setId_Ubicacion(Integer.parseInt(Gestionar_Almacenn.txtIDAlmacen.getText()));
    
    
    
    
    String sql = "  UPDATE almacen SET pasillo=?,ala=?,estante=?,nivel=?,capacidad=?  WHERE id_Ubicacion =?";
    
    try {
        this.conectar();
        
        ps = this.con.prepareStatement(sql);
          ps.setString(1, A.getPasillo());
        ps.setString(2, A.getAla());
        ps.setString(3, A.getEstante());
        ps.setInt(4, A.getNivel());
        ps.setInt(5, A.getCapacidad());
        ps.setInt(6, A.getId_Ubicacion());
        
        int fila = ps.executeUpdate();
        
        if (fila > 0) {
      
              JOptionPane.showMessageDialog(null, "Actualizacion exitosa");
              MostrarAlmacen();
              
          ct.CancelarActualizaciondeAlmacen();
              
              
        } else {
            JOptionPane.showMessageDialog(null, "Error al Actualizar ");
        }
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, " Error" + e.getMessage());
    }  finally {
        
        this.cerrarCn();
    }
    
}


public  void EliminarAlmacen ( int Id_Ubicacion) throws  ClassNotFoundException,SQLException {
    
    
    String sql = "DELETE FROM almacen where id_Ubicacion =?";
    
    try {
        this.conectar();
        
        ps = this.con.prepareStatement(sql);
         ps.setInt(1, Id_Ubicacion);
         
         if (ps.executeUpdate() > 0) {
             
             JOptionPane.showMessageDialog(null, " Ubicacion Eliminada correctamente");
            
        }
         
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, " Error al Eliminada Alamacen " + e.getMessage());
    }  finally {
        this.cerrarCn();
    }
    
   
    
    
}




    
    
    
    
    
    
    
    
    
    
    
public void EliminarConSeguridad() {
    int filaSeleccionada = Gestionar_Almacenn.TablaAlmacen.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Por favor seleccione una Ubicación si la desea eliminar");
        return; // 🔴 salir del método, así no sigue ejecutando
    }

    int id_Ubicacion = Integer.parseInt(Gestionar_Almacenn.TablaAlmacen.getValueAt(filaSeleccionada, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Está seguro de querer eliminar esta ubicación?",
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        try {
            EliminarAlmacen(id_Ubicacion);
            MostrarAlmacen(); // puedes llamar directamente aquí
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlmacenDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}












    




public void MostrarAlmacen() throws ClassNotFoundException, SQLException {

    TituloAlmacen();
    limpiarTablaAlmacen();

    ArrayList<Almacen> LsitaTerminada = ListaDeAlamcen();

    modeloAlmacen = (DefaultTableModel) Gestionar_Almacenn.TablaAlmacen.getModel();

    // Ahora necesitamos 7 columnas (id, pasillo, ala, estante, capacidad, producto, stock)
    Object[] obj = new Object[9];

    for (int i = 0; i < LsitaTerminada.size(); i++) {
        obj[0] = LsitaTerminada.get(i).getId_Ubicacion();
        obj[1] = LsitaTerminada.get(i).getPasillo();
        obj[2] = LsitaTerminada.get(i).getAla();
        obj[3] = LsitaTerminada.get(i).getEstante();
        obj [4] =LsitaTerminada.get(i).getNivel();
        obj[5] = LsitaTerminada.get(i).getCapacidad();
        obj[6] = LsitaTerminada.get(i).getProducto();
        obj[7] = LsitaTerminada.get(i).getStock();

        modeloAlmacen.addRow(obj);
    }

    Gestionar_Almacenn.TablaAlmacen.setModel(modeloAlmacen);

    centrarTextoTabla(Gestionar_Almacenn.TablaAlmacen);

}













private ArrayList<Almacen> ListaDeAlamcen() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> ListaCompleta = new ArrayList<>();

    String sql = "SELECT  " +
            "a.id_Ubicacion, " +
            "a.pasillo AS Pasillo_Almacen, " +
            "a.ala, " +
            "a.estante, " +
            "a.nivel, " +
            "a.capacidad, " +
            "p.nombre AS nombreProducto, " +
            "i.Cantidad_Disponible " +
            "FROM almacen a " +
            "LEFT JOIN inventario i ON a.id_Ubicacion = i.id_Ubicacion " +  // relación por id_Ubicacion
            "LEFT JOIN producto p ON i.idProducto = p.idProducto";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Almacen al = new Almacen();
            al.setId_Ubicacion(rs.getInt("id_Ubicacion"));
            al.setPasillo(rs.getString("Pasillo_Almacen"));
            al.setAla(rs.getString("ala"));
            al.setEstante(rs.getString("estante"));
            al.setNivel(rs.getInt("nivel"));
            al.setCapacidad(rs.getInt("capacidad"));
            al.setProducto(rs.getString("nombreProducto"));
            al.setStock(rs.getString("Cantidad_Disponible"));

            ListaCompleta.add(al);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del almacén: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaCompleta;
}



        
public void CargarComboxBoxAlmecenInventarioLiquido() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> almacens = this.ListaDeAlamcen();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Almacen> almacensUnicos = new LinkedHashSet<>(almacens);

    // Limpiamos el ComboBox
    InventarioLiquido.jComboUbicacion.removeAllItems();

    // Agregamos solo combinaciones únicas de pasillo y estante
    for (Almacen al : almacensUnicos) {
        InventarioLiquido.jComboUbicacion.addItem(al);
    }
}

    
    

public void agregarBuscadorAlmacenInventarioLiquido() {
    if (InventarioLiquido.txtBuscarUbicacion!= null) {
       InventarioLiquido.txtBuscarUbicacion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioLiquido();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioLiquido();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioLiquido();
            }
        });
    }
}


   
    

private void actualizarComboBoxInventarioLiquido() {
    String texto = InventarioLiquido.txtBuscarUbicacion.getText().toLowerCase();

    try {
        // Lista completa de almacenes
        ArrayList<Almacen> lista = this.ListaDeAlamcen();

        // Limpiar ComboBox
        InventarioLiquido.jComboUbicacion.removeAllItems();

        // Agregar coincidencias por pasillo, estante, capacidad, nivel o ala
        for (Almacen p : lista) {
            String pasillo   = p.getPasillo()   != null ? p.getPasillo().toLowerCase()   : "";
            String estante   = p.getEstante()   != null ? p.getEstante().toLowerCase()   : "";
            String ala       = p.getAla()       != null ? p.getAla().toLowerCase()       : "";
            String capacidad = String.valueOf(p.getCapacidad());
            String nivel     = String.valueOf(p.getNivel());

            if (pasillo.contains(texto) || 
                estante.contains(texto) || 
                ala.contains(texto) ||
                capacidad.contains(texto) || 
                nivel.contains(texto)) {

               InventarioLiquido.jComboUbicacion.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}




public void agregarFiltroBusqueda() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (Gestionar_Almacenn.txtBuscarAlmacen != null && Gestionar_Almacenn.TablaAlmacen != null) {

        // Agregamos un listener persistente
       Gestionar_Almacenn.txtBuscarAlmacen.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscar();
            }

            private void buscar() {
                String texto = Gestionar_Almacenn.txtBuscarAlmacen.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) Gestionar_Almacenn.TablaAlmacen.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) Gestionar_Almacenn.TablaAlmacen.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    Gestionar_Almacenn.TablaAlmacen.setRowSorter(sorter);
                }

                // Aplicar o limpiar filtro
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    } catch (PatternSyntaxException ex) {
                        sorter.setRowFilter(null); // evita errores si hay símbolos especiales
                    }
                }
            }
        });
    }
}










private void actualizarComboBoxSolido() {
    String texto = InventarioSolido.txtBuscarUbicacion.getText().toLowerCase();

    try {
        // Lista completa de almacenes
        ArrayList<Almacen> lista = this.ListaDeAlamcen();

        // Limpiar ComboBox
     InventarioSolido.jComboUbicacionSolido.removeAllItems();

        // Agregar coincidencias por pasillo, estante, capacidad, nivel o ala
        for (Almacen p : lista) {
            String pasillo   = p.getPasillo()   != null ? p.getPasillo().toLowerCase()   : "";
            String estante   = p.getEstante()   != null ? p.getEstante().toLowerCase()   : "";
            String ala       = p.getAla()       != null ? p.getAla().toLowerCase()       : "";
            String capacidad = String.valueOf(p.getCapacidad());
            String nivel     = String.valueOf(p.getNivel());

            if (pasillo.contains(texto) || 
                estante.contains(texto) || 
                ala.contains(texto) ||
                capacidad.contains(texto) || 
                nivel.contains(texto)) {

               InventarioSolido.jComboUbicacionSolido.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}




        
public void CargarComboxBoxAlmecenSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> almacens = this.ListaDeAlamcen();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Almacen> almacensUnicos = new LinkedHashSet<>(almacens);
    if (InventarioSolido.jComboUbicacionSolido != null) {
        
    
    // Limpiamos el ComboBox
    InventarioSolido.jComboUbicacionSolido.removeAllItems();

    // Agregamos solo combinaciones únicas de pasillo y estante
    for (Almacen al : almacensUnicos) {
         InventarioSolido.jComboUbicacionSolido.addItem(al);
    }
    }
}

    

public void agregarBuscadorAlmacenSolido() {
    if (InventarioSolido.txtBuscarUbicacion!= null) {
      InventarioSolido.txtBuscarUbicacion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxSolido();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxSolido();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxSolido();
            }
        });
    }
}





private void actualizarComboBoxUnidad() {
    String texto = InventarioUnidad.txtBuscarUbicacion.getText().toLowerCase();

    try {
        // Lista completa de almacenes
        ArrayList<Almacen> lista = this.ListaDeAlamcen();

        // Limpiar ComboBox
       InventarioUnidad.jComboBoxUbicacion.removeAllItems();

        // Agregar coincidencias por pasillo, estante, capacidad, nivel o ala
        for (Almacen p : lista) {
            String pasillo   = p.getPasillo()   != null ? p.getPasillo().toLowerCase()   : "";
            String estante   = p.getEstante()   != null ? p.getEstante().toLowerCase()   : "";
            String ala       = p.getAla()       != null ? p.getAla().toLowerCase()       : "";
            String capacidad = String.valueOf(p.getCapacidad());
            String nivel     = String.valueOf(p.getNivel());

            if (pasillo.contains(texto) || 
                estante.contains(texto) || 
                ala.contains(texto) ||
                capacidad.contains(texto) || 
                nivel.contains(texto)) {

                 InventarioUnidad.jComboBoxUbicacion.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}




        
public void CargarComboxBoxAlmecenuUNIDAD() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> almacens = this.ListaDeAlamcen();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Almacen> almacensUnicos = new LinkedHashSet<>(almacens);

    // Limpiamos el ComboBox
InventarioUnidad.jComboBoxUbicacion.removeAllItems();

    // Agregamos solo combinaciones únicas de pasillo y estante
    for (Almacen al : almacensUnicos) {
        InventarioUnidad.jComboBoxUbicacion.addItem(al);
    }
}

    

public void agregarBuscadorAlmacenUNIDAD() {
    if (  InventarioUnidad.txtBuscarUbicacion!= null) {
       InventarioUnidad.txtBuscarUbicacion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxUnidad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxUnidad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxUnidad();
            }
        });
    }
}



}
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    

