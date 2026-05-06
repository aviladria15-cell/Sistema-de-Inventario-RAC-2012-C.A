/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Funciones.Alerte_De_Stock;
import ModeloDAO.AlmacenDao;
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioLiquidoDao;
import ModeloDAO.MovientosDao;
import ModeloDAO.ProductoDao;
import ModeloDAO.ProveedorDao;
import Validacion.Validar_Inventario_Liquido;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_GestionInventario.InventarioLiquido;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author avila
 */
public class Controlador_Inventario_Liquido implements ActionListener{

    
   private  final  Menu_Sistema menu;
   private InventarioLiquido Inventario_Liquido;
   
   public  Controlador_Inventario_Liquido (Menu_Sistema menu){
       this.menu=menu;
   }
    
    
    public  void MostrarVistaInventarioLiquido(){
        if (Inventario_Liquido == null) {
            Inventario_Liquido=new InventarioLiquido();
            
            //AgregarListeners
            AgregarListeners();
        }
        
        Inventario_Liquido.setVisible(true);
      
        CargarVista();
       
        
        CalcularPrecioVentaLiquido();
        IniciarPrecioVenta();
        CargarComboxCuentas();
        IniciarVista();
        
        Validacion.Validar_Inventario_Liquido inventario_Liquido = new Validar_Inventario_Liquido();
     
     inventario_Liquido.inicializarValidacionesLiquido();
        
        menu.setVisible(false);
    }
    
    
    
    
    
    private  void AgregarListeners (){
        
     Inventario_Liquido.btbVolverMenu.addActionListener(this);
     Inventario_Liquido.btbModificar.addActionListener(this);
     Inventario_Liquido.btbHistorialMovimiento.addActionListener(this);
     Inventario_Liquido.btbInformacionLote.addActionListener(this);
     Inventario_Liquido.btbRegistrar.addActionListener(this);
     Inventario_Liquido.btbOcultarHistorial.addActionListener(this);
     Inventario_Liquido.btbOcultarLote.addActionListener(this);
     Inventario_Liquido.btbCancelarActualizacion.addActionListener(this);
     Inventario_Liquido.btbConfirmarActualizacion.addActionListener(this);
     Inventario_Liquido.btbModificarLote.addActionListener(this);
     Inventario_Liquido.btbConfirmarModificacionLote.addActionListener(this);
     Inventario_Liquido.btbCancelarModificacionLote.addActionListener(this);
        
    }
    
    
   
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Inventario_Liquido.btbVolverMenu) {
            VolverAlmenu();
            
        }
        
        else if (e.getSource() == Inventario_Liquido.btbRegistrar) {
            RegistrarProductoLiquido();
        } 
        
        else if (e.getSource() == Inventario_Liquido.btbHistorialMovimiento){
            VerHistoriaDeMovimiento();
        } 
        
        else if (e.getSource() == Inventario_Liquido.btbOcultarHistorial){
            OcultarHistorial();
        }  
        
        else if (e.getSource() == Inventario_Liquido.btbInformacionLote){
            
            MostrarInformacionLote();
            
            
        }
        
        else if (e.getSource() == Inventario_Liquido.btbOcultarLote){
            EsconderLote();
        }  
        
        else if (e.getSource() == Inventario_Liquido.btbModificar){
            SeleccioanrProductoLiquido();
        }
        
        else if (e.getSource() == Inventario_Liquido.btbCancelarActualizacion){
            CancelarActualiciconProducto();
        }
        
        else if (e.getSource() == Inventario_Liquido.btbConfirmarActualizacion){
           ConfirmarActualizacion();
        }
        
        else if (e.getSource() == Inventario_Liquido.btbModificarLote){
            
            try {
                SeleccionarLote();
            } catch (ParseException ex) {
                System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }    
        
        else if (e.getSource() == Inventario_Liquido.btbCancelarModificacionLote){
            CancelarActualizacionLote();
        }
        
        else if (e.getSource() == Inventario_Liquido.btbConfirmarModificacionLote){
            ConfirmarActualizacionLote();
        }
        
       
        
  
        
    }
    
    
    private  void ConfirmarActualizacionLote(){
          Validar_Inventario_Liquido validar_Inventario_Liquido = new Validar_Inventario_Liquido();
      
       try {
           validar_Inventario_Liquido. ActualizarDatosLoteLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
        CargarComboxCuentas();
       
     
    }
    
    
    
    private  void CancelarActualizacionLote (){
        
         Inventario_Liquido.btbOcultarLote.setVisible(true);
    Inventario_Liquido.btbModificarLote.setVisible(true);
    
    Inventario_Liquido.btbConfirmarModificacionLote.setVisible(false);
    Inventario_Liquido.btbCancelarModificacionLote.setVisible(false);
    
    Inventario_Liquido.txtCantidadActual.setText("");
    Inventario_Liquido.txtCostoUnitario.setText("");
    Inventario_Liquido.txtPrecioVenta.setText("");
    Inventario_Liquido.jDateFechaVencimiento.setDate(null);
        
    }
    
    
    private  void SeleccionarLote () throws ParseException{
        
       int filla = Inventario_Liquido.TablaLote.getSelectedRow();
       
       if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Debe seleccionar un lote para modificarlo.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
    return;
} else {
           String mensaje = "<html><b>¿Estás seguro de actualizar los siguientes campos?</b><br><br>" +
                 "• Cantidad<br>" +
                 "• Costo Unitario<br>" +
                 "• Precio Venta <br>" +
                 "• Proveedor<br>"  +
                 "• Fecha Vencimiento</html>" ;

int respuesta = JOptionPane.showConfirmDialog(null, 
    mensaje, 
    "Confirmar modificación", 
    JOptionPane.YES_NO_OPTION, 
    JOptionPane.QUESTION_MESSAGE);

if (respuesta == JOptionPane.YES_OPTION) {
   
    Inventario_Liquido.btbOcultarLote.setVisible(false);
    Inventario_Liquido.btbModificarLote.setVisible(false);
    
    Inventario_Liquido.btbConfirmarModificacionLote.setVisible(true);
    Inventario_Liquido.btbCancelarModificacionLote.setVisible(true);
    
    Inventario_Liquido.txtCantidadActual.setText(Inventario_Liquido.TablaLote.getValueAt(filla, 2).toString());
     
     Inventario_Liquido.txtCostoUnitario.setText(Inventario_Liquido.TablaLote.getValueAt(filla, 6).toString());
    //  Inventario_Liquido.jDateFechaVencimiento.setDate((Date)Inventario_Liquido.TablaLote.getValueAt(filla,5));


          String fechaStr = InventarioLiquido.TablaLote.getValueAt(filla, 5).toString();
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
    java.util.Date fecha = formato.parse(fechaStr);
    

   InventarioLiquido.jDateFechaVencimiento.setDate(fecha);
         





// Aquí continúa con el código para modificar el producto
} else {
    return; // el usuario canceló
}
       }
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private  void ConfirmarActualizacion (){
         Validar_Inventario_Liquido validar_Inventario_Liquido = new Validar_Inventario_Liquido();
         
       try {
           validar_Inventario_Liquido.ActualizarEndradaLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
        Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
       alerte_De_Stock.AlertaLiquido() ;
    }
    
    
    private  void CancelarActualiciconProducto (){
        Inventario_Liquido.btbConfirmarActualizacion.setVisible(false);
   Inventario_Liquido.btbCancelarActualizacion.setVisible(false);
   
   
   Inventario_Liquido.btbModificar.setVisible(true);
   Inventario_Liquido.btbHistorialMovimiento.setVisible(true);
   Inventario_Liquido.btbInformacionLote.setVisible(true);
   Inventario_Liquido.btbRegistrar.setVisible(true);
   Inventario_Liquido.txtStockMaximo.setText("");
   Inventario_Liquido.txtStockMinimo.setText("");
   
    }    
    
    
    private  void SeleccioanrProductoLiquido (){
              
   int filla = InventarioLiquido.TablaLiquido.getSelectedRow();

if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Debe seleccionar un producto para modificarlo.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
    return;
} else {
    String mensaje = "<html><b>¿Estás seguro de actualizar los siguientes campos?</b><br><br>" +
                 "• Stock Mínimo<br>" +
                 "• Stock Máximo<br>" +
                 "• Ubicación<br>" +
                 "• Producto</html>";

int respuesta = JOptionPane.showConfirmDialog(null, 
    mensaje, 
    "Confirmar modificación", 
    JOptionPane.YES_NO_OPTION, 
    JOptionPane.QUESTION_MESSAGE);

if (respuesta == JOptionPane.YES_OPTION) {
   
   Inventario_Liquido.btbModificar.setVisible(false);
   Inventario_Liquido.btbHistorialMovimiento.setVisible(false);
   Inventario_Liquido.btbInformacionLote.setVisible(false);
   Inventario_Liquido.btbRegistrar.setVisible(false);
    
   Inventario_Liquido.btbConfirmarActualizacion.setVisible(true);
   Inventario_Liquido.btbCancelarActualizacion.setVisible(true);
   
   
  seleccionarItemCombo(Inventario_Liquido.jComboProductoLiquido, Inventario_Liquido.TablaLiquido.getValueAt(filla, 1).toString());
   Inventario_Liquido.txtStockMinimo.setText(Inventario_Liquido.TablaLiquido.getValueAt(filla, 3).toString());
   
    Inventario_Liquido.txtStockMaximo.setText(Inventario_Liquido.TablaLiquido.getValueAt(filla, 4).toString());
   Object valorUbicacion = Inventario_Liquido.TablaLiquido.getValueAt(filla, 5);
seleccionarItemCombo(Inventario_Liquido.jComboUbicacion, valorUbicacion);
 
    
    
  
    
    
} else {
    return; // el usuario canceló
}
}
        
        
        
    }
    
    
    
    
    
    private   void VolverAlmenu (){
        
        menu.setVisible(true);
        
        Inventario_Liquido.setVisible(false);
    }
    
    
    private  void EsconderLote (){
         Inventario_Liquido.jScrollPaneInformacionLote.setVisible(false);
    
    Inventario_Liquido.btbOcultarLote.setVisible(false);
    
    Inventario_Liquido.btbModificar.setVisible(true);
    Inventario_Liquido.btbHistorialMovimiento.setVisible(true);
    Inventario_Liquido.btbInformacionLote.setVisible(true);
    Inventario_Liquido.btbRegistrar.setVisible(true);
 
    Inventario_Liquido.JcroPanelPrincipal.setVisible(true);
    
     Inventario_Liquido.txtBuscarLote.setVisible(false);
     Inventario_Liquido.txtBuscarProductoLiquidoTabla.setText("");
        
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setVisible(true);
        Inventario_Liquido.btbModificarLote.setVisible(false);
    }
    
    
    
  private  void MostrarInformacionLote (){
    Inventario_Liquido.jScrollPaneInformacionLote.setVisible(true);
        Inventario_Liquido.txtBuscarLote.setVisible(true);
        
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setVisible(false);
        
        Inventario_Liquido.btbModificarLote.setVisible(true);
        
    Inventario_Liquido.btbOcultarLote.setVisible(true);
    
    
    Inventario_Liquido.btbModificar.setVisible(false);
    Inventario_Liquido.btbHistorialMovimiento.setVisible(false);
    Inventario_Liquido.btbInformacionLote.setVisible(false);
    Inventario_Liquido.btbRegistrar.setVisible(false);
     Inventario_Liquido.txtBuscarLote.setText("");
    Inventario_Liquido.JcroPanelPrincipal.setVisible(false);
    
    
    
    
    ModeloDAO.InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
    
       try {
           inventarioLiquidoDao.MostrarVerInventarioLiquidoLote();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       inventarioLiquidoDao.agregarFiltroBusquedalOTE();
      
  }
  
  
  
    
    
     private  void RegistrarProductoLiquido (){
    Validar_Inventario_Liquido validar_Inventario_Liquido = new Validar_Inventario_Liquido();
       try {
           validar_Inventario_Liquido.ValidarRegistroDeEntradaLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
   
       Funciones.Alerte_De_Stock alerte_De_Stock = new  Alerte_De_Stock();
       
       alerte_De_Stock.AlertaLiquido();
       
       
       CargarComboxCuentas();
      
     }
    
    
    
    private  void IniciarVista (){
        Inventario_Liquido.btbOcultarHistorial.setVisible(false);
        Inventario_Liquido.jScrollPaneHistorial.setVisible(false);
       Inventario_Liquido.jScrollPaneInformacionLote.setVisible(false);
       Inventario_Liquido.btbOcultarLote.setVisible(false);
        Inventario_Liquido.JcroPanelPrincipal.setVisible(true);
        Inventario_Liquido.txtBuscarLote.setText("");
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setText("");
        Inventario_Liquido.txtBuscarLote.setVisible(false);
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setVisible(true);
        
         Inventario_Liquido.btbModificar.setVisible(true);
        Inventario_Liquido.btbHistorialMovimiento.setVisible(true);
        Inventario_Liquido.btbInformacionLote.setVisible(true);
        Inventario_Liquido.btbRegistrar.setVisible(true);
        
        Inventario_Liquido.txtBuscarHistorial.setVisible(false);
        
          Inventario_Liquido.txtBuscarHistorial.setText("");
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setText("");
        Inventario_Liquido.txtStockMaximo.setText("");
        Inventario_Liquido.txtStockMinimo.setText("");
        
        Inventario_Liquido.btbConfirmarActualizacion.setVisible(false);
   Inventario_Liquido.btbCancelarActualizacion.setVisible(false);
   Inventario_Liquido.btbModificarLote.setVisible(false);
   Inventario_Liquido.btbConfirmarModificacionLote.setVisible(false);
   Inventario_Liquido.btbCancelarModificacionLote.setVisible(false);
        
    }
    
    
    
    
    
    
    private  void OcultarHistorial (){
        Inventario_Liquido.jScrollPaneHistorial.setVisible(false);
        
         Inventario_Liquido.btbModificar.setVisible(true);
        Inventario_Liquido.btbHistorialMovimiento.setVisible(true);
        Inventario_Liquido.btbInformacionLote.setVisible(true);
        Inventario_Liquido.btbRegistrar.setVisible(true);
        
        Inventario_Liquido.btbOcultarHistorial.setVisible(false);
        Inventario_Liquido.JcroPanelPrincipal.setVisible(true);
        
         Inventario_Liquido.txtBuscarHistorial.setVisible(false);
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setVisible(true);
          Inventario_Liquido.txtBuscarHistorial.setText("");
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setText("");
        
        
        
    }
    
    
    
    
      private  void VerHistoriaDeMovimiento (){
        Inventario_Liquido.JcroPanelPrincipal.setVisible(false);
         
        Inventario_Liquido.btbModificar.setVisible(false);
        Inventario_Liquido.btbHistorialMovimiento.setVisible(false);
        Inventario_Liquido.btbInformacionLote.setVisible(false);
        Inventario_Liquido.btbRegistrar.setVisible(false);
        
        Inventario_Liquido.txtBuscarHistorial.setVisible(true);
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setVisible(false);
        
        Inventario_Liquido.btbOcultarHistorial.setVisible(true);
        
        Inventario_Liquido.jScrollPaneHistorial.setVisible(true);
        Inventario_Liquido.txtBuscarHistorial.setText("");
        Inventario_Liquido.txtBuscarProductoLiquidoTabla.setText("");
        
        ModeloDAO.MovientosDao movientosDao = new MovientosDao();
        
       try {
           movientosDao.MostrarMovimientoLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       movientosDao. agregarFiltroBusquedaLiquidoHistario();
        
    }
    
    
    
    private  void CargarVista (){
        ModeloDAO.ProductoDao productoDao = new ProductoDao();
       try {
           productoDao. cargarProductosEnComboLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       
       productoDao.agregarBuscadorProducto();
       
       ModeloDAO.ProveedorDao proveedorDao = new ProveedorDao();
       try {
           proveedorDao.cargarProveedoresInventarioLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       proveedorDao.agregarBuscadorProveedorInventarioLiquido();
       
       
       ModeloDAO.AlmacenDao almacenDao = new AlmacenDao();
       try {
           almacenDao.CargarComboxBoxAlmecenInventarioLiquido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       almacenDao.agregarBuscadorAlmacenInventarioLiquido();
       
    ModeloDAO.InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
    
       try {
           inventarioLiquidoDao. MostrarVerInventarioLiquido();
            
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
    
       inventarioLiquidoDao. agregarFiltroBusquedaLiquido();
       
       
       Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
       alerte_De_Stock.AlertaLiquido() ;
     
    }
    
    
  private boolean actualizandoPrecio = false;   // ← Bandera para evitar recursión

private void CalcularPrecioVentaLiquido() {
    // Si ya estamos actualizando, salimos para evitar bucle infinito
    if (actualizandoPrecio) {
        return;
    }

    try {
        actualizandoPrecio = true;   // Activamos la bandera

        double precioUnitario = Double.parseDouble(
            Inventario_Liquido.txtCostoUnitario.getText().trim());

        double porcentaje = Double.parseDouble(
            Inventario_Liquido.txtPorcentaje.getText().trim());

        double precioVentaFinal = precioUnitario + (precioUnitario * (porcentaje / 100.0));

        // Formatear con 2 decimales
        String textoFormateado = String.format(Locale.US, "%.2f", precioVentaFinal);

        // Actualizamos el campo de precio de venta
        Inventario_Liquido.txtPrecioVenta.setText(textoFormateado);

    } catch (NumberFormatException e) {
        // Si hay error (campos vacíos o texto inválido), limpiamos el precio
        Inventario_Liquido.txtPrecioVenta.setText("");
    } finally {
        actualizandoPrecio = false;   // Siempre liberamos la bandera
    }
}
    
private void IniciarPrecioVenta() {

    // Listener para el campo de PORCENTAJE
    Inventario_Liquido.txtPorcentaje.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CalcularPrecioVentaLiquido();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CalcularPrecioVentaLiquido();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CalcularPrecioVentaLiquido();
        }
    });

    // Listener para el campo de COSTO UNITARIO
    Inventario_Liquido.txtCostoUnitario.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CalcularPrecioVentaLiquido();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CalcularPrecioVentaLiquido();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CalcularPrecioVentaLiquido();
        }
    });

}

private  void CargarComboxCuentas (){
    ModeloDAO.CuentaDao cuentasDao = new CuentaDao();
    
       try {
           cuentasDao.cargarComboCuentasInventarioo(Inventario_Liquido.jComboCuentaDebitar);
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       try {
           cuentasDao.cargarComboCuentasPasivo(Inventario_Liquido.jComboCuentaAcreditar);
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Inventario_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
        
}

    

private void seleccionarItemCombo(JComboBox combo, Object valor) {
    if (valor == null) {
        if (combo != null) combo.setSelectedIndex(0);
        return;
    }

    // Si es el combo de Ubicación, usamos el método especializado
    if (combo == Inventario_Liquido.jComboUbicacion) {
        seleccionarUbicacionEnCombo(valor.toString().trim());
        return;
    }

    // Lógica normal para otros combos (Producto, etc.)
    String valorStr = valor.toString().trim().toLowerCase();

    for (int i = 0; i < combo.getItemCount(); i++) {
        Object item = combo.getItemAt(i);
        if (item == null) continue;

        String itemStr = item.toString().trim().toLowerCase();

        if (itemStr.equals(valorStr) || itemStr.contains(valorStr) || valorStr.contains(itemStr)) {
            combo.setSelectedIndex(i);
            return;
        }
    }
    combo.setSelectedIndex(0);
}
 
 
private void seleccionarUbicacionEnCombo(String textoTabla) {
    
    JComboBox combo = Inventario_Liquido.jComboUbicacion;
    
    if (textoTabla == null || textoTabla.trim().isEmpty()) {
        combo.setSelectedIndex(0);
        return;
    }

    String buscado = textoTabla.toLowerCase().trim();
    int mejorIndice = -1;
    int mejorPuntaje = -1;

    for (int i = 0; i < combo.getItemCount(); i++) {
        Object item = combo.getItemAt(i);
        if (item == null) continue;

        String itemStr = item.toString().toLowerCase().trim();

        // Contamos cuántas partes coinciden (Ala, Pasillo, Estante, Nivel, Cap)
        int puntaje = 0;
        String[] partes = buscado.split("\\|");
        
        for (String parte : partes) {
            parte = parte.trim().toLowerCase();
            if (parte.length() > 2 && itemStr.contains(parte)) {
                puntaje++;
            }
        }

        // Bonus si coincide casi todo el texto
        if (itemStr.contains(buscado) || buscado.contains(itemStr)) {
            puntaje += 5;
        }

        if (puntaje > mejorPuntaje) {
            mejorPuntaje = puntaje;
            mejorIndice = i;
        }
    }

    if (mejorIndice != -1) {
        combo.setSelectedIndex(mejorIndice);
       
    } else {
     
        combo.setSelectedIndex(0);
    }
} 
    
    
    

}
