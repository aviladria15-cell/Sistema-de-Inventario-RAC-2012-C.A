

package Controlador;

import Funciones.Alerte_De_Stock;
import Funciones.BCV;

import java.awt.Font;

import javax.swing.UIManager;

import Vista_Usuari_Empleado.frm_GestionarEmpleado;
import Vista_Usuari_Empleado.Login_Ingreso;
import Vista_Almacen.Gestionar_Almacenn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import javax.swing.Timer;
import Vista_Usuari_Empleado.Menu_Sistema;
import javax.swing.JOptionPane;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.Insets;
import ModeloDAO.ProveedorDao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;

public class controladorVisual {
   
   
   


Funciones.Alerte_De_Stock alerta = new Alerte_De_Stock(); // Instancia desde el paquete de funciones para traer las alerta de los diferentes productos

  // intacias de la clase proveedor para actualizar la tabla con los datos t
   
  
       

public  void  Menu_Empleado (frm_GestionarEmpleado Em){
    
 Em.setVisible(true);
    
}




public  void CerrarSeccion (Menu_Sistema M){


M.setVisible(false);
Login_Ingreso.txtPasContraseña.setText("");
Login_Ingreso.txtUsuario.setText("");
    
}

 
 
 
 // Este metodo para iniciar la hora en la vista del menu del sistema para tres el dia mes anos hora y segundos
    public void IniciarHora() {
    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalDateTime ahora = LocalDateTime.now();
            
            // Formato completo: jueves, 19 de noviembre de 2025  03:45:28 PM
            DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                "EEEE, dd 'de' MMMM 'de' yyyy    hh:mm:ss a", Locale.forLanguageTag("es-ES"));
            
            Menu_Sistema.lblHora.setText(ahora.format(formato));
        }
    });
    timer.start();
}


// Este metodo para inicar la hora en el menu del login
 public void IniciarHoraLogin() {
    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalDateTime ahora = LocalDateTime.now();
            
            // Formato completo: jueves, 19 de noviembre de 2025  03:45:28 PM
            DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                "EEEE, dd 'de' MMMM 'de' yyyy    hh:mm:ss a", Locale.forLanguageTag("es-ES"));
            
            Login_Ingreso.lblHora.setText(ahora.format(formato));
        }
    });
    timer.start();
}
 
 


 
 
 // este metodo para para validar que el usuario seleccione una fila de la tabla almacen obtener los datos de la tablas
public  void ActualizarAlmacen(){
    
    
   int fila =   Gestionar_Almacenn.TablaAlmacen.getSelectedRow();
   
   
    if ( fila == -1) {
    JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para actualizar los datos");
    } else {
    
        if (fila != -1) {
          

 Gestionar_Almacenn.btbRegistrarAlmacen.setVisible(false);
 Gestionar_Almacenn.btbEliminarAlmacen.setVisible(false);
 Gestionar_Almacenn.btbActualizarALmacen.setVisible(false);
 Gestionar_Almacenn.btbConfirmarActualizacion.setVisible(true);
 Gestionar_Almacenn.btbCancelarActualizacion.setVisible(true);
 Gestionar_Almacenn.txtIDAlmacen.setVisible(true);
Gestionar_Almacenn.txtPasillolmacen.setText(Gestionar_Almacenn.TablaAlmacen.getValueAt(fila, 1).toString());
Gestionar_Almacenn.ComboxAla.setSelectedItem (Gestionar_Almacenn.TablaAlmacen.getValueAt(fila, 2).toString());
Gestionar_Almacenn.txtEstanteAlmacen.setText(Gestionar_Almacenn.TablaAlmacen.getValueAt(fila,3).toString());
Gestionar_Almacenn.txtNivel.setText(Gestionar_Almacenn.TablaAlmacen.getValueAt(fila,4).toString());
Gestionar_Almacenn.txtCapacidadAlmacen.setText(Gestionar_Almacenn.TablaAlmacen.getValueAt(fila, 5).toString());
Gestionar_Almacenn.txtIDAlmacen.setText(Gestionar_Almacenn.TablaAlmacen.getValueAt(fila, 0).toString());

        }
    
    }
    
    
    
    
    
    
}


 // este metodo es para cancelar la actualizacion del almacen y todo vuelva a sus lugar
public  void  CancelarActualizaciondeAlmacen (){
 
    
    Gestionar_Almacenn.btbRegistrarAlmacen.setVisible(true);
 Gestionar_Almacenn.btbEliminarAlmacen.setVisible(true);
 Gestionar_Almacenn.btbActualizarALmacen.setVisible(true);
 Gestionar_Almacenn.btbConfirmarActualizacion.setVisible(false);
 Gestionar_Almacenn.btbCancelarActualizacion.setVisible(false);
 
 Gestionar_Almacenn.txtIDAlmacen.setVisible(false);
 
    
    
    Gestionar_Almacenn.txtPasillolmacen.setText("");
    Gestionar_Almacenn.txtNivel.setText("");
    Gestionar_Almacenn.txtEstanteAlmacen.setText("");
    Gestionar_Almacenn.txtCapacidadAlmacen.setText("");
    Gestionar_Almacenn.txtEstanteAlmacen.setText("");
    
    
    
    
}




// este metodo es el que le da el estilo al sistema a los campos de texto y botones para ver una interfaz diferentes a la de netbeans
public void Estilo() {
    try {
        // Look & Feel moderno estilo macOS
        UIManager.setLookAndFeel(new FlatMacLightLaf());
        FlatLaf.registerCustomDefaultsSource("recursos");
        FlatLaf.setUseNativeWindowDecorations(true);

        // Fuente elegante y legible
        UIManager.put("defaultFont", new Font("San Francisco", Font.PLAIN, 15));

        // Curvatura general para todo
        int arc = 18;
        UIManager.put("Component.arc", arc);
        UIManager.put("Button.arc", arc);
        UIManager.put("TextComponent.arc", arc);
        UIManager.put("ProgressBar.arc", arc);

        // Colores principales estilo Apple
        Color accent = new Color(0, 122, 255);   // Azul macOS
        Color accentHover = new Color(0, 122, 255, 30);
        Color accentBorder = new Color(0, 122, 255, 160);
        Color panelBg = new Color(242, 242, 247); // Fondo más claro estilo macOS
        Color tableAlternateRow = new Color(235, 245, 255); // Azul celeste claro para filas cebra
        Color tableGrid = new Color(210, 210, 215); // Líneas de grilla neutras

        // Fondo general
        UIManager.put("Panel.background", panelBg);

        // ---- Botones ----
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", new Color(28, 28, 30));
        UIManager.put("Button.focusWidth", 1);
        UIManager.put("Button.innerFocusWidth", 1);
        UIManager.put("Button.padding", new Insets(12, 24, 12, 24));
        UIManager.put("Button.hoverBackground", accentHover);
        UIManager.put("Button.hoverBorderColor", accentBorder);
        UIManager.put("Button.hoverForeground", Color.WHITE); // Letras blancas al pasar a transparente
        // Fondo transparente con efecto arcoíris claro al seleccionar
        UIManager.put("Button.selectionBackground", new Color(255, 255, 255, 50)); // Base transparente
        UIManager.put("Button.focusColor", new Color(255, 182, 193, 180)); // Rosa claro inicial
        UIManager.put("Button.focusedBorderColor", new Color(173, 216, 230, 180)); // Azul claro para el borde
        UIManager.put("Button.disabledBackground", new Color(240, 240, 240));
        UIManager.put("Button.disabledText", new Color(140, 140, 140));

        // ---- Campos de texto ----
        UIManager.put("TextComponent.selectionBackground", accent);
        UIManager.put("TextComponent.selectionForeground", Color.WHITE);
        UIManager.put("TextComponent.placeholderForeground", new Color(170, 170, 170));
        UIManager.put("TextComponent.focusWidth", 1);

        // ---- Barra de progreso ----
        UIManager.put("ProgressBar.arc", 12);
        UIManager.put("ProgressBar.trackColor", new Color(235, 235, 240));
        UIManager.put("ProgressBar.selectionForeground", accent);

        // ---- Scrollbars ----
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumb", new Color(200, 200, 200, 140));
        UIManager.put("ScrollBar.thumbHover", new Color(170, 170, 170, 160));

        // ---- Bordes generales ----
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.borderColor", new Color(210, 210, 215));
// ---- Tablas ----
        UIManager.put("Table.gridColor", new Color(210, 210, 215)); // Líneas de grilla neutras
        UIManager.put("Table.background", Color.WHITE); // Fondo base blanco para filas pares
        UIManager.put("Table.alternateRowColor", new Color(0, 191, 255)); // Azul celeste fuerte para filas impares
        UIManager.put("Table.foreground", new Color(28, 28, 30)); // Texto oscuro
        UIManager.put("Table.selectionBackground", new Color(0, 122, 255)); // Selección azul macOS-like
        UIManager.put("Table.selectionForeground", Color.WHITE); // Texto blanco al seleccionar
        UIManager.put("TableHeader.background", new Color(242, 242, 247)); // Fondo del encabezado
        UIManager.put("TableHeader.foreground", new Color(28, 28, 30)); // Texto del encabezado
        UIManager.put("TableHeader.separatorColor", new Color(210, 210, 215)); // Separador del encabezado
    } catch (Exception e) {
        System.err.println("Error al aplicar estilo Apple-like");
        e.printStackTrace();
    }
}





/// Desde aqui es para la Gestion de inventario para el inventario de liquido


/**
 * Método auxiliar para seleccionar un ítem en un JComboBox
 * Funciona para combos con ítems concatenados por "|" y donde el valor de la tabla
 * puede ser solo una parte de alguna de las secciones.
 */

// para obtner el datos ncorrecto de la tabla al comnbos 




















// Metodo para las saluidas ajuste ect todos sus cosas desde el inventario




public  void RegistrarSalidaDelAlmacendeSOLIDO (){
    
   Gestionar_Almacenn.lblPasillo.setVisible(false);
   Gestionar_Almacenn.txtPasillolmacen.setVisible(false);
  Gestionar_Almacenn.lblNivel.setVisible(false);
  Gestionar_Almacenn.txtNivel.setVisible(false);
  Gestionar_Almacenn.lblALA.setVisible(false);
  Gestionar_Almacenn.ComboxAla.setVisible(false);
  Gestionar_Almacenn.lblCaoacidad.setVisible(false);
  Gestionar_Almacenn.txtCapacidadAlmacen.setVisible(false);
  Gestionar_Almacenn.lblEstante.setVisible(false);
  Gestionar_Almacenn.txtEstanteAlmacen.setVisible(false);
  Gestionar_Almacenn.jScrollPaneAlmacen.setVisible(false);
  Gestionar_Almacenn.lblBuscarAlmacen.setVisible(false);
  Gestionar_Almacenn.txtBuscarAlmacen.setVisible(false);
  Gestionar_Almacenn.btbRegistrarAlmacen.setVisible(false);
  Gestionar_Almacenn.btbActualizarALmacen.setVisible(false);
  Gestionar_Almacenn.btbEliminarAlmacen.setVisible(false);
  Gestionar_Almacenn.btbCancelarActualizacion.setVisible(false);
  Gestionar_Almacenn.btbConfirmarActualizacion.setVisible(false);
        Gestionar_Almacenn.lblPasilloValidacion.setVisible(false);
        Gestionar_Almacenn.lblNivelValidacion.setVisible(false);
        Gestionar_Almacenn.lblEstanteValidacion.setVisible(false);
        Gestionar_Almacenn.lblCapacidadValidacion.setVisible(false);
        Gestionar_Almacenn.lblAlaValidacion.setVisible(false);
  

  
  
  
       
  
  
  
}


public  void  VOLVERLALMACEN (){
    

   
    Gestionar_Almacenn.txtDolarHoy.setVisible(false);
    Gestionar_Almacenn.lblTasaDelDolar.setVisible(false);
    

    
    
      Gestionar_Almacenn.lblPasillo.setVisible(true);
   Gestionar_Almacenn.txtPasillolmacen.setVisible(true);
  Gestionar_Almacenn.lblNivel.setVisible(true);
  Gestionar_Almacenn.txtNivel.setVisible(true);
  Gestionar_Almacenn.lblALA.setVisible(true);
  Gestionar_Almacenn.ComboxAla.setVisible(true);
  Gestionar_Almacenn.lblCaoacidad.setVisible(true);
  Gestionar_Almacenn.txtCapacidadAlmacen.setVisible(true);
  Gestionar_Almacenn.lblEstante.setVisible(true);
  Gestionar_Almacenn.txtEstanteAlmacen.setVisible(true);
  Gestionar_Almacenn.jScrollPaneAlmacen.setVisible(true);
  Gestionar_Almacenn.lblBuscarAlmacen.setVisible(true);
  Gestionar_Almacenn.txtBuscarAlmacen.setVisible(true);
  Gestionar_Almacenn.btbRegistrarAlmacen.setVisible(true);
  Gestionar_Almacenn.btbActualizarALmacen.setVisible(true);
  Gestionar_Almacenn.btbEliminarAlmacen.setVisible(true);
  Gestionar_Almacenn.btbCancelarActualizacion.setVisible(false);
  Gestionar_Almacenn.btbConfirmarActualizacion.setVisible(false);
  
     
}



public  void SALIDASolido (){
    
    
    Gestionar_Almacenn.lblTotalEnBolivares.setVisible(true);
    Gestionar_Almacenn.txtTotalEnBolivares.setVisible(true);
    Gestionar_Almacenn.lblTotalEnDolares.setVisible(true);
    Gestionar_Almacenn.txtTotalEnDolares.setVisible(true);
    
    Gestionar_Almacenn.txtDolarHoy.setVisible(true);
    Gestionar_Almacenn.lblTasaDelDolar.setVisible(true);
    
         Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.setVisible(true);
    
    Gestionar_Almacenn.lblCuentas.setVisible(true);
        Gestionar_Almacenn.lblcuentaIngreso.setVisible(true);
        Gestionar_Almacenn.jComboBoxCuentas.setVisible(true);
        Gestionar_Almacenn.jComboBoxCuentasIngreso.setVisible(true);
    
    

    
   Gestionar_Almacenn.  lblAccion.setVisible(true);
  Gestionar_Almacenn.   txtSALIDA.setVisible(true);
    
   Gestionar_Almacenn. lblTIPOPRODUCTO.setVisible(true);
   Gestionar_Almacenn.  txtTIPOLIQUIDO.setVisible(true);
     
     
    Gestionar_Almacenn.  lblProducto.setVisible(true);
    Gestionar_Almacenn. JComboxSOLIDO.setVisible(true);
     
    Gestionar_Almacenn.txtCantidadSolido.setVisible(true);
    Gestionar_Almacenn.lblValidarCantidadSolido.setVisible(true);
    Gestionar_Almacenn.lblValidadarComboxSolido.setVisible(true);
    Gestionar_Almacenn.txtDatelleSolido.setVisible(true);
    Gestionar_Almacenn.lblValidarDetalleSOLIDO.setVisible(true);
    
    Gestionar_Almacenn.lblValidacionPrecioVentaSolido.setVisible(true);
    
    
      
    Gestionar_Almacenn.lblCantidad.setVisible(true);
    Gestionar_Almacenn.lblPrecioVenta.setVisible(true);
    Gestionar_Almacenn.lblDatelle.setVisible(true);
   Gestionar_Almacenn.   btbREALIZARSALIDASOLIDO.setVisible(true);
   
   
   Gestionar_Almacenn. btbRealizarAjuste.setVisible(false);
    
    
}




public  void MENU (){
    Gestionar_Almacenn.txtCantidadSolido.setText("");
    Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.setText("");
    Gestionar_Almacenn.txtCantidadUnidad.setText("");
    Gestionar_Almacenn.txtPrecioVentaUnidad.setText("");
    
    Gestionar_Almacenn.txtTotalEnBolivares.setText("");
    Gestionar_Almacenn.txtTotalEnDolares.setText("");
    Gestionar_Almacenn.txtCantidadLiquido.setText("");
    Gestionar_Almacenn.txtPrecioVentaLiquido.setText("");
    Gestionar_Almacenn.txtDatelleLiquido.setText("");
    Gestionar_Almacenn.txtDolarHoy.setVisible(false);
    Gestionar_Almacenn.lblTasaDelDolar.setVisible(false);
     Gestionar_Almacenn.lblTotalEnBolivares.setVisible(false);
    Gestionar_Almacenn.txtTotalEnBolivares.setVisible(false);
  
    Gestionar_Almacenn.lblTotalEnDolares.setVisible(false);
    Gestionar_Almacenn.txtTotalEnDolares.setVisible(false);
    
     Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO.setVisible(false);
    Gestionar_Almacenn.lblValidadarComboxLIQUIDOAJUSTE1.setVisible(false);
    Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO.setVisible(false);
    Gestionar_Almacenn.txtCantidadAjusteLIQUIDO.setVisible(false);
    Gestionar_Almacenn.txtDatelleAjsuteLIQUIDO.setVisible(false);
    
    Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD.setVisible(false);
    Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD.setVisible(false);
    Gestionar_Almacenn.lblValidadarComboxUNIDADAJUSTE.setVisible(false);
    Gestionar_Almacenn.txtCantidadAjusteUNIDAD.setVisible(false);
    Gestionar_Almacenn.txtDatelleAjsuteUNIDAD.setVisible(false);
    
    
    Gestionar_Almacenn.txtCantidadAjusteSolido.setVisible(false);
    Gestionar_Almacenn.txtDatelleAjsuteSolido.setVisible(false);
    Gestionar_Almacenn.lblValidadarComboxSolidoAjuste.setVisible(false);
    Gestionar_Almacenn.lblValidarCantidadAjusteSolido.setVisible(false);
     Gestionar_Almacenn.lblValidarDetalleAjusteSolido.setVisible(false);
    
    Gestionar_Almacenn.txtCantidadSolido.setVisible(false);
    Gestionar_Almacenn.lblValidarCantidadSolido.setVisible(false);
    Gestionar_Almacenn.lblValidadarComboxSolido.setVisible(false);
    Gestionar_Almacenn.txtDatelleSolido.setVisible(false);
    Gestionar_Almacenn.lblValidarDetalleSOLIDO.setVisible(false);
    Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.setVisible(false);
    Gestionar_Almacenn.lblValidacionPrecioVentaSolido.setVisible(false);
    
    Gestionar_Almacenn.txtPrecioVentaUnidad.setText("");
    Gestionar_Almacenn.txtPrecioVentaUnidad.setVisible(false);
    Gestionar_Almacenn.lblValidacionPrecioVentaUnidad.setVisible(false);
    Gestionar_Almacenn.txtCantidadUnidad.setVisible(false);
    Gestionar_Almacenn.lblValidarCantidadAjusteSolido.setVisible(false);
    Gestionar_Almacenn.txtDatelleUnidad.setVisible(false);
    Gestionar_Almacenn.lblValidarDetalleUnidad.setVisible(false);
    
     Gestionar_Almacenn.   btbRealizarAjusteUnidad.setVisible(false);
   Gestionar_Almacenn.    txtTIPOLIQUIDOOOOOO.setVisible(false); 
    
    
    Gestionar_Almacenn.lblCuentas.setVisible(false);
     Gestionar_Almacenn.jComboBoxCuentasInventario.setVisible(false);
     
    Gestionar_Almacenn.lblCuentas.setVisible(false);
        Gestionar_Almacenn.lblcuentaIngreso.setVisible(false);
        Gestionar_Almacenn.jComboBoxCuentas.setVisible(false);
        Gestionar_Almacenn.jComboBoxCuentasIngreso.setVisible(false);
    
    
     Gestionar_Almacenn.  lblAccion.setVisible(false);
   Gestionar_Almacenn.  txtSALIDA.setVisible(false);
    
    
  Gestionar_Almacenn.  lblTIPOPRODUCTO.setVisible(false);
  Gestionar_Almacenn.   txtTIPOLIQUIDO.setVisible(false);
     
   Gestionar_Almacenn.   lblProducto.setVisible(false);
   Gestionar_Almacenn.  JComboxSOLIDO.setVisible(false);

     
   Gestionar_Almacenn.  lblCantidad.setVisible(false);
   Gestionar_Almacenn.  txtCantidadLiquido.setVisible(false);
    
   Gestionar_Almacenn.  lblDatelle.setVisible(false);
    Gestionar_Almacenn. txtDatelleLiquido.setVisible(false);
    
   Gestionar_Almacenn.  lblPrecioVenta.setVisible(false);
     Gestionar_Almacenn. txtPrecioVentaLiquido.setVisible(false);
     
      Gestionar_Almacenn. btbREALIZARSALIDASOLIDO.setVisible(false);
      
      Gestionar_Almacenn.txtAjuste.setVisible(false);
      
      
     Gestionar_Almacenn. btbRealizarAjuste.setVisible(false);
     
     Gestionar_Almacenn.txtTIPOUNIDAD.setVisible(false);
      Gestionar_Almacenn.btbRealizarSalidaUnidad.setVisible(false);
      
      Gestionar_Almacenn.JComboxSUnidad.setVisible(false);
      
     Gestionar_Almacenn.  btbREALIZARSALIDALIQUIDOoooo.setVisible(false);
    Gestionar_Almacenn.  JComboxLiquido.setVisible(false);
    
    Gestionar_Almacenn. btbRealizarAjusteLiquido.setVisible(false);
    
    Gestionar_Almacenn.lblValidacionPrecioVentaLiquido.setVisible(false);
    Gestionar_Almacenn.lblValidadarComboxLiquido.setVisible(false);
    Gestionar_Almacenn.lblValidarCantidadLiquido.setVisible(false);
    Gestionar_Almacenn.lblValidarDetalleLiquyido.setVisible(false);
    
}







public  void AjustesSolido(){
    
    
         

     Gestionar_Almacenn.lblCuentas.setVisible(true);
     Gestionar_Almacenn.jComboBoxCuentasInventario.setVisible(true);
     
    
    
    
    
    Gestionar_Almacenn.lblProducto.setVisible(true);
    Gestionar_Almacenn.JComboxSOLIDO.setVisible(true);
    
    Gestionar_Almacenn.lblAccion.setVisible(true);
    
Gestionar_Almacenn.lblTIPOPRODUCTO.setVisible(true);
Gestionar_Almacenn.txtTIPOLIQUIDO.setVisible(true);

Gestionar_Almacenn.lblDatelle.setVisible(true);
Gestionar_Almacenn.txtDatelleAjsuteSolido.setVisible(true);
Gestionar_Almacenn.lblValidadarComboxSolidoAjuste.setVisible(true);
Gestionar_Almacenn.lblValidarCantidadAjusteSolido.setVisible(true);
Gestionar_Almacenn.lblValidarDetalleAjusteSolido.setVisible(true);

Gestionar_Almacenn.lblCantidad.setVisible(true);
Gestionar_Almacenn.txtCantidadAjusteSolido.setVisible(true);
  
Gestionar_Almacenn.txtAjuste.setVisible(true);
    
    
    
    
    Gestionar_Almacenn. btbRealizarAjuste.setVisible(true);
   
    
}





public  void SalidaUnidadAlmacen (){
     Gestionar_Almacenn.lblTotalEnBolivares.setVisible(true);
    Gestionar_Almacenn.txtTotalEnBolivares.setVisible(true);
    Gestionar_Almacenn.lblTotalEnDolares.setVisible(true);
    Gestionar_Almacenn.txtTotalEnDolares.setVisible(true);
    
    Gestionar_Almacenn.txtDolarHoy.setVisible(true);
    Gestionar_Almacenn.lblTasaDelDolar.setVisible(true);
      Gestionar_Almacenn.txtPrecioVentaUnidad.setVisible(true);
    Gestionar_Almacenn.lblValidacionPrecioVentaUnidad.setVisible(true);
    Gestionar_Almacenn.txtCantidadUnidad.setVisible(true);
    Gestionar_Almacenn.lblValidarCantidadAjusteSolido.setVisible(true);
    Gestionar_Almacenn.txtDatelleUnidad.setVisible(true);
    Gestionar_Almacenn.lblValidarDetalleUnidad.setVisible(true);
    Gestionar_Almacenn.lblValidadarComboxUnidad.setVisible(true);
  
    
  
      Gestionar_Almacenn.lblCuentas.setVisible(true);
        Gestionar_Almacenn.lblcuentaIngreso.setVisible(true);
        Gestionar_Almacenn.jComboBoxCuentas.setVisible(true);
        Gestionar_Almacenn.jComboBoxCuentasIngreso.setVisible(true);
    

     
     Gestionar_Almacenn.lblAccion.setVisible(true);
     Gestionar_Almacenn.txtSALIDA.setVisible(true);
     
     
     Gestionar_Almacenn.lblTIPOPRODUCTO.setVisible(true);
     Gestionar_Almacenn.lblProducto.setVisible(true);
    
     Gestionar_Almacenn.lblCantidad.setVisible(true);
     Gestionar_Almacenn.lblDatelle.setVisible(true);
   Gestionar_Almacenn.lblPrecioVenta.setVisible(true);
    
     
    
     Gestionar_Almacenn.txtTIPOUNIDAD.setVisible(true);
    
    
      Gestionar_Almacenn.btbRealizarSalidaUnidad.setVisible(true);
      
        Gestionar_Almacenn.JComboxSUnidad.setVisible(true);
    
   
}



public  void AjusteUnidad(){
     Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD.setVisible(true);
    Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD.setVisible(true);
    Gestionar_Almacenn.lblValidadarComboxUNIDADAJUSTE.setVisible(true);
    Gestionar_Almacenn.txtCantidadAjusteUNIDAD.setVisible(true);
    Gestionar_Almacenn.txtDatelleAjsuteUNIDAD.setVisible(true);
    

     Gestionar_Almacenn.lblCuentas.setVisible(true);
     Gestionar_Almacenn.jComboBoxCuentasInventario.setVisible(true);
     

     
     Gestionar_Almacenn.lblAccion.setVisible(true);
     Gestionar_Almacenn.txtAjuste.setVisible(true);
     Gestionar_Almacenn.lblTIPOPRODUCTO.setVisible(true);
     Gestionar_Almacenn.txtTIPOUNIDAD.setVisible(true);
     Gestionar_Almacenn.lblProducto.setVisible(true);
     Gestionar_Almacenn.JComboxSUnidad.setVisible(true);
     Gestionar_Almacenn.lblCantidad.setVisible(true);
   
     Gestionar_Almacenn.lblDatelle.setVisible(true);
     
  Gestionar_Almacenn.   btbRealizarAjusteUnidad.setVisible(true);
    
    
    
    
    
    
    
    
    
    
    
}

public  void SalidaLiquido(){
   
     Gestionar_Almacenn.lblTotalEnBolivares.setVisible(true);
    Gestionar_Almacenn.txtTotalEnBolivares.setVisible(true);
    Gestionar_Almacenn.lblTotalEnDolares.setVisible(true);
    Gestionar_Almacenn.txtTotalEnDolares.setVisible(true);
    
    Gestionar_Almacenn.txtDolarHoy.setVisible(true);
    Gestionar_Almacenn.lblTasaDelDolar.setVisible(true);
   
      Gestionar_Almacenn.lblCuentas.setVisible(true);
        Gestionar_Almacenn.lblcuentaIngreso.setVisible(true);
        Gestionar_Almacenn.jComboBoxCuentas.setVisible(true);
        Gestionar_Almacenn.jComboBoxCuentasIngreso.setVisible(true);
    

     
     Gestionar_Almacenn.lblAccion.setVisible(true);
     Gestionar_Almacenn.txtSALIDA.setVisible(true);
     
     Gestionar_Almacenn.lblTIPOPRODUCTO.setVisible(true);
     Gestionar_Almacenn.lblProducto.setVisible(true);
     Gestionar_Almacenn.lblCantidad.setVisible(true);
     Gestionar_Almacenn.lblDatelle.setVisible(true);
     Gestionar_Almacenn.lblPrecioVenta.setVisible(true);
     Gestionar_Almacenn.txtCantidadLiquido.setVisible(true);
     Gestionar_Almacenn.txtDatelleLiquido.setVisible(true);
     Gestionar_Almacenn.txtPrecioVentaLiquido.setVisible(true);
     
     
     
    Gestionar_Almacenn.txtTIPOLIQUIDOOOOOO.setVisible(true);
    
    
    
    
       
     Gestionar_Almacenn.  btbREALIZARSALIDALIQUIDOoooo.setVisible(true);
    Gestionar_Almacenn.  JComboxLiquido.setVisible(true);
    
    
  
    
}

public  void AjusteLiquido (){
    
    
   
 
     Gestionar_Almacenn.lblCuentas.setVisible(true);
     Gestionar_Almacenn.jComboBoxCuentasInventario.setVisible(true);
    
  
    
       
     
     Gestionar_Almacenn.lblAccion.setVisible(true);
     Gestionar_Almacenn.txtAjuste.setVisible(true);
     Gestionar_Almacenn.lblTIPOPRODUCTO.setVisible(true);
     Gestionar_Almacenn.txtTIPOLIQUIDOOOOOO.setVisible(true);
     Gestionar_Almacenn.lblProducto.setVisible(true);
     Gestionar_Almacenn.JComboxLiquido.setVisible(true);
     Gestionar_Almacenn.lblCantidad.setVisible(true);
     
     Gestionar_Almacenn.lblDatelle.setVisible(true);
    
     
    Gestionar_Almacenn. btbRealizarAjusteLiquido.setVisible(true);
    
    Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO.setVisible(true);
    Gestionar_Almacenn.lblValidadarComboxLIQUIDOAJUSTE1.setVisible(true);
    Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO.setVisible(true);
    Gestionar_Almacenn.txtCantidadAjusteLIQUIDO.setVisible(true);
    Gestionar_Almacenn.txtDatelleAjsuteLIQUIDO.setVisible(true);
    
    
    
    
    
    
    
    
    
}






// En tu clase Controlador
private boolean listenersConfigurados = false;
 private boolean listenersConfiguradosSolido = false;
 private boolean listenersConfiguradosUnidad = false;
public void CalcularTotal() {
    // ===============================================================
    // 1. CONFIGURAR LISTENERS (SOLO LA PRIMERA VEZ)
    // ===============================================================
    if (!listenersConfigurados) {
        DocumentListener listener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { CalcularTotal(); }
            @Override public void removeUpdate(DocumentEvent e) { CalcularTotal(); }
            @Override public void changedUpdate(DocumentEvent e) { CalcularTotal(); }
        };

        Gestionar_Almacenn.txtCantidadLiquido.getDocument().addDocumentListener(listener);
        Gestionar_Almacenn.txtPrecioVentaLiquido.getDocument().addDocumentListener(listener);
        Gestionar_Almacenn.txtDolarHoy.getDocument().addDocumentListener(listener);

        Gestionar_Almacenn.txtTotalEnBolivares.setEditable(false);
        Gestionar_Almacenn.txtTotalEnDolares.setEditable(false);

        // INICIA LA CARGA DE LA TASA
      //  BCV.mostrarTasaEnCampo(Gestionar_Almacen.txtDolarHoy);

        listenersConfigurados = true;
    }

    // ===============================================================
    // 2. LEER CANTIDAD
    // ===============================================================
    double cantidad = 0;
    try {
        String texto = Gestionar_Almacenn.txtCantidadLiquido.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) cantidad = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 3. LEER PRECIO EN DÓLARES
    // ===============================================================
    double precioUSD = 0;
    try {
        String texto = Gestionar_Almacenn.txtPrecioVentaLiquido.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) precioUSD = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 4. LEER TASA DEL DÓLAR (IGNORA "Cargando...")
    // ===============================================================
    double tasaDolar = 1;
    try {
        String texto = Gestionar_Almacenn.txtDolarHoy.getText()
                .replaceAll("<[^>]+>", "")  // Quita HTML
                .trim();

        if (texto.equalsIgnoreCase("Cargando...") || texto.isEmpty()) {
            tasaDolar = BCV.leerDeCache(); // Usa caché si está cargando
        } else {
            texto = texto.replace(",", ".");
            tasaDolar = Double.parseDouble(texto);
            if (tasaDolar <= 0) tasaDolar = BCV.leerDeCache();
        }
    } catch (Exception e) {
        tasaDolar = BCV.leerDeCache(); // En error, usa caché
    }

    // ===============================================================
    // 5. CALCULAR TOTALES
    // ===============================================================
    double totalDolares = cantidad * precioUSD;
    double totalBolivares = totalDolares * tasaDolar;

    // ===============================================================
    // 6. FORMATO VENEZOLANO
    // ===============================================================
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "VE"));
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

    // ===============================================================
    // 7. MOSTRAR RESULTADOS
    // ===============================================================
    Gestionar_Almacenn.txtTotalEnBolivares.setText("Bs. " + df.format(totalBolivares));
    Gestionar_Almacenn.txtTotalEnDolares.setText("$ " + String.format("%.2f", totalDolares));
}



public void CalcularTotalSolido() {
    // ===============================================================
    // 1. CONFIGURAR LISTENERS (SOLO LA PRIMERA VEZ)
    // ===============================================================
    if (!listenersConfiguradosSolido) {
        DocumentListener listener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { CalcularTotalSolido(); }
            @Override public void removeUpdate(DocumentEvent e) { CalcularTotalSolido(); }
            @Override public void changedUpdate(DocumentEvent e) { CalcularTotalSolido(); }
        };

        Gestionar_Almacenn.txtCantidadSolido.getDocument().addDocumentListener(listener);
        Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.getDocument().addDocumentListener(listener);
        Gestionar_Almacenn.txtDolarHoy.getDocument().addDocumentListener(listener);

        Gestionar_Almacenn.txtTotalEnBolivares.setEditable(false);
        Gestionar_Almacenn.txtTotalEnDolares.setEditable(false);

        // INICIA LA CARGA DE LA TASA
      //  BCV.mostrarTasaEnCampo(Gestionar_Almacen.txtDolarHoy);

        listenersConfiguradosSolido = true;
    }

    // ===============================================================
    // 2. LEER CANTIDAD
    // ===============================================================
    double cantidad = 0;
    try {
        String texto = Gestionar_Almacenn.txtCantidadSolido.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) cantidad = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 3. LEER PRECIO EN DÓLARES
    // ===============================================================
    double precioUSD = 0;
    try {
        String texto = Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) precioUSD = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 4. LEER TASA DEL DÓLAR (IGNORA "Cargando...")
    // ===============================================================
    double tasaDolar = 1;
    try {
        String texto = Gestionar_Almacenn.txtDolarHoy.getText()
                .replaceAll("<[^>]+>", "")  // Quita HTML
                .trim();

        if (texto.equalsIgnoreCase("Cargando...") || texto.isEmpty()) {
            tasaDolar = BCV.leerDeCache(); // Usa caché si está cargando
        } else {
            texto = texto.replace(",", ".");
            tasaDolar = Double.parseDouble(texto);
            if (tasaDolar <= 0) tasaDolar = BCV.leerDeCache();
        }
    } catch (Exception e) {
        tasaDolar = BCV.leerDeCache(); // En error, usa caché
    }

    // ===============================================================
    // 5. CALCULAR TOTALES
    // ===============================================================
    double totalDolares = cantidad * precioUSD;
    double totalBolivares = totalDolares * tasaDolar;

    // ===============================================================
    // 6. FORMATO VENEZOLANO
    // ===============================================================
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "VE"));
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

    // ===============================================================
    // 7. MOSTRAR RESULTADOS
    // ===============================================================
    Gestionar_Almacenn.txtTotalEnBolivares.setText("Bs. " + df.format(totalBolivares));
    Gestionar_Almacenn.txtTotalEnDolares.setText("$ " + String.format("%.2f", totalDolares));
}



public void CalcularTotalUnidad() {
    // ===============================================================
    // 1. CONFIGURAR LISTENERS (SOLO LA PRIMERA VEZ)
    // ===============================================================
    if (!listenersConfiguradosUnidad) {
        DocumentListener listener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { CalcularTotalUnidad(); }
            @Override public void removeUpdate(DocumentEvent e) { CalcularTotalUnidad(); }
            @Override public void changedUpdate(DocumentEvent e) { CalcularTotalUnidad(); }
        };

        Gestionar_Almacenn.txtCantidadUnidad.getDocument().addDocumentListener(listener);
        Gestionar_Almacenn.txtPrecioVentaUnidad.getDocument().addDocumentListener(listener);
        Gestionar_Almacenn.txtDolarHoy.getDocument().addDocumentListener(listener);

        Gestionar_Almacenn.txtTotalEnBolivares.setEditable(false);
        Gestionar_Almacenn.txtTotalEnDolares.setEditable(false);

        // INICIA LA CARGA DE LA TASA
      //  BCV.mostrarTasaEnCampo(Gestionar_Almacen.txtDolarHoy);

        listenersConfiguradosUnidad = true;
    }

    // ===============================================================
    // 2. LEER CANTIDAD
    // ===============================================================
    double cantidad = 0;
    try {
        String texto = Gestionar_Almacenn.txtCantidadUnidad.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) cantidad = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 3. LEER PRECIO EN DÓLARES
    // ===============================================================
    double precioUSD = 0;
    try {
        String texto = Gestionar_Almacenn.txtPrecioVentaUnidad.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) precioUSD = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 4. LEER TASA DEL DÓLAR (IGNORA "Cargando...")
    // ===============================================================
    double tasaDolar = 1;
    try {
        String texto = Gestionar_Almacenn.txtDolarHoy.getText()
                .replaceAll("<[^>]+>", "")  // Quita HTML
                .trim();

        if (texto.equalsIgnoreCase("Cargando...") || texto.isEmpty()) {
            tasaDolar = BCV.leerDeCache(); // Usa caché si está cargando
        } else {
            texto = texto.replace(",", ".");
            tasaDolar = Double.parseDouble(texto);
            if (tasaDolar <= 0) tasaDolar = BCV.leerDeCache();
        }
    } catch (Exception e) {
        tasaDolar = BCV.leerDeCache(); // En error, usa caché
    }

    // ===============================================================
    // 5. CALCULAR TOTALES
    // ===============================================================
    double totalDolares = cantidad * precioUSD;
    double totalBolivares = totalDolares * tasaDolar;

    // ===============================================================
    // 6. FORMATO VENEZOLANO
    // ===============================================================
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "VE"));
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

    // ===============================================================
    // 7. MOSTRAR RESULTADOS
    // ===============================================================
    Gestionar_Almacenn.txtTotalEnBolivares.setText("Bs. " + df.format(totalBolivares));
    Gestionar_Almacenn.txtTotalEnDolares.setText("$ " + String.format("%.2f", totalDolares));
}
}
