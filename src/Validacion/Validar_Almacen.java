/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Modelo.Almacen;
import ModeloDAO.AlmacenDao;
import Vista_Almacen.Gestionar_Almacenn;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Validar_Almacen extends AlmacenDao {

    AlmacenDao dao = new AlmacenDao();

    // ------------------- INICIALIZAR VALIDACIONES -------------------
   public void inicializarValidacionesAlmacen() {
    // Ocultar errores
    ocultarTodosErroresAlmacen();

    // === APLICAR FILTROS ===
    aplicarFiltroAlfanumericoConGuion(Gestionar_Almacenn.txtPasillolmacen);
    aplicarFiltroSoloNumeros(Gestionar_Almacenn.txtNivel);
    aplicarFiltroSoloNumeros(Gestionar_Almacenn.txtEstanteAlmacen);
    aplicarFiltroSoloNumeros(Gestionar_Almacenn.txtCapacidadAlmacen);

    // === VALIDACIÓN EN TIEMPO REAL ===
    Gestionar_Almacenn.txtPasillolmacen.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarPasillo));
    
    Gestionar_Almacenn.txtNivel.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarNivel));
    
    Gestionar_Almacenn.txtEstanteAlmacen.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarEstante));
    
    Gestionar_Almacenn.txtCapacidadAlmacen.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarCapacidad));
    
    Gestionar_Almacenn.ComboxAla.addActionListener(e -> validarAla());
}

   
   
   private void aplicarFiltroAlfanumericoConGuion(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("[a-zA-Z0-9\\- ]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("[a-zA-Z0-9\\- ]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}
   
   
   private void aplicarFiltroSoloNumeros(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}
    // ------------------- VALIDACIONES -------------------

   private void validarPasillo() {
    String pasillo = Gestionar_Almacenn.txtPasillolmacen.getText().trim();
    if (pasillo.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblPasilloValidacion, "El pasillo no puede estar vacío");
    } else if (pasillo.length() < 2) {
        mostrarError(Gestionar_Almacenn.lblPasilloValidacion, "Mínimo 2 caracteres");
    } else if (tieneRepeticionesExtrañas(pasillo)) {
        mostrarError(Gestionar_Almacenn.lblPasilloValidacion, "Evita repeticiones (ej: aaa, 111)");
    } else {
        ocultarError(Gestionar_Almacenn.lblPasilloValidacion);
    }
}

   private void validarNivel() {
    String nivel = Gestionar_Almacenn.txtNivel.getText().trim();
    if (nivel.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblNivelValidacion, "El nivel no puede estar vacío");
    } else if (nivel.length() > 3) {
        mostrarError(Gestionar_Almacenn.lblNivelValidacion, "Máximo 3 dígitos");
    } else if (tieneRepeticionesExtrañas(nivel)) {
        mostrarError(Gestionar_Almacenn.lblNivelValidacion, "Evita repeticiones (ej: 111)");
    } else {
        ocultarError(Gestionar_Almacenn.lblNivelValidacion);
    }
}
   private void validarEstante() {
    String estante = Gestionar_Almacenn.txtEstanteAlmacen.getText().trim();
    if (estante.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblEstanteValidacion, "El estante no puede estar vacío");
    } else if (estante.length() > 3) {
        mostrarError(Gestionar_Almacenn.lblEstanteValidacion, "Máximo 3 dígitos");
    } else if (tieneRepeticionesExtrañas(estante)) {
        mostrarError(Gestionar_Almacenn.lblEstanteValidacion, "Evita repeticiones (ej: 999)");
    } else {
        ocultarError(Gestionar_Almacenn.lblEstanteValidacion);
    }
}

   private void validarCapacidad() {
    String capacidad = Gestionar_Almacenn.txtCapacidadAlmacen.getText().trim();
    if (capacidad.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblCapacidadValidacion, "La capacidad no puede estar vacía");
    } else {
        try {
            int cap = Integer.parseInt(capacidad);
            if (cap <= 0) {
                mostrarError(Gestionar_Almacenn.lblCapacidadValidacion, "Debe ser mayor a 0");
            } else if (cap > 10000) {
                mostrarError(Gestionar_Almacenn.lblCapacidadValidacion, "Máximo 10000");
            } else if (tieneRepeticionesExtrañas(capacidad)) {
                mostrarError(Gestionar_Almacenn.lblCapacidadValidacion, "Evita repeticiones (ej: 1111)");
            } else {
                ocultarError(Gestionar_Almacenn.lblCapacidadValidacion);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblCapacidadValidacion, "Número inválido");
        }
    }
}
   private void validarAla() {
    Object alaSel = Gestionar_Almacenn.ComboxAla.getSelectedItem();
    String ala = (alaSel != null) ? alaSel.toString().trim() : "";
    if (ala.isEmpty() || ala.equalsIgnoreCase("Seleccione")) {
        mostrarError(Gestionar_Almacenn.lblAlaValidacion, "Debe seleccionar un ala");
    } else {
        ocultarError(Gestionar_Almacenn.lblAlaValidacion);
    }
}
   private boolean tieneRepeticionesExtrañas(String texto) {
    return texto.matches(".*(.)\\1{2}.*"); // 3 o más caracteres iguales
}
   
   private void ocultarTodosErroresAlmacen() {
    ocultarError(Gestionar_Almacenn.lblPasilloValidacion);
    ocultarError(Gestionar_Almacenn.lblNivelValidacion);
    ocultarError(Gestionar_Almacenn.lblEstanteValidacion);
    ocultarError(Gestionar_Almacenn.lblCapacidadValidacion);
    ocultarError(Gestionar_Almacenn.lblAlaValidacion);
}
   
   @FunctionalInterface
interface RunnableTask { void run(); }

static class SimpleDocumentListener implements DocumentListener {
    private final RunnableTask task;
    public SimpleDocumentListener(RunnableTask task) { this.task = task; }
    public void insertUpdate(DocumentEvent e) { task.run(); }
    public void removeUpdate(DocumentEvent e) { task.run(); }
    public void changedUpdate(DocumentEvent e) { task.run(); }
}
    // ------------------- VALIDAR FORMULARIO COMPLETO -------------------
    public boolean validarFormularioAlmacen() {
        validarPasillo();
        validarNivel();
        validarEstante();
        validarCapacidad();
        validarAla();

        return !Gestionar_Almacenn.lblPasilloValidacion.isVisible()
                && !Gestionar_Almacenn.lblNivelValidacion.isVisible()
                && !Gestionar_Almacenn.lblEstanteValidacion.isVisible()
                && !Gestionar_Almacenn.lblCapacidadValidacion.isVisible()
                && !Gestionar_Almacenn.lblAlaValidacion.isVisible();
    }

    // ------------------- REGISTRAR -------------------
    public void registrarAlmacenValidado() throws ClassNotFoundException, SQLException {
        if (!validarFormularioAlmacen()) {
            JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de registrar el almacén.");
            return;
        }

        Almacen a = new Almacen();
        a.setPasillo(Gestionar_Almacenn.txtPasillolmacen.getText().trim());
        a.setNivel(Integer.parseInt(Gestionar_Almacenn.txtNivel.getText().trim()));
        a.setEstante(Gestionar_Almacenn.txtEstanteAlmacen.getText().trim());
        a.setCapacidad(Integer.parseInt(Gestionar_Almacenn.txtCapacidadAlmacen.getText().trim()));
        a.setAla(Gestionar_Almacenn.ComboxAla.getSelectedItem().toString().trim());

        dao.RegistrarAlmacen();
      
    }

    // ------------------- ACTUALIZAR -------------------
    public void actualizarAlmacenValidado() throws ClassNotFoundException, SQLException {
        if (!validarFormularioAlmacen()) {
            JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de actualizar el almacén.");
            return;
        }

        Almacen a = new Almacen();
        a.setId_Ubicacion(Integer.parseInt(Gestionar_Almacenn.txtIDAlmacen.getText().trim()));
        a.setPasillo(Gestionar_Almacenn.txtPasillolmacen.getText().trim());
        a.setNivel(Integer.parseInt(Gestionar_Almacenn.txtNivel.getText().trim()));
        a.setEstante(Gestionar_Almacenn.txtEstanteAlmacen.getText().trim());
        a.setCapacidad(Integer.parseInt(Gestionar_Almacenn.txtCapacidadAlmacen.getText().trim()));
        a.setAla(Gestionar_Almacenn.ComboxAla.getSelectedItem().toString().trim());

        dao.ActualizarAlmacen();
       
    }

 

    private boolean esTextoSinSentido(String texto) {
        // Detecta combinaciones ilógicas tipo "uu22" o "aa11" o exceso de mezcla
        return texto.matches(".*([a-zA-Z]{2,}[0-9]{2,}|[0-9]{2,}[a-zA-Z]{2,}).*");
    }

    // ------------------- MÉTODOS DE ERROR -------------------
    private void mostrarError(javax.swing.JLabel lbl, String mensaje) {
        lbl.setText(mensaje);
        lbl.setVisible(true);
    }

    private void ocultarError(javax.swing.JLabel lbl) {
        lbl.setVisible(false);
    }
}
