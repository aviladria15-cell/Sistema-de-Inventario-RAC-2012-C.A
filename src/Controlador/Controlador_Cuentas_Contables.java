/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.CuentaDao;
import java.awt.event.ActionListener;
import vista_Libro_Contable.Frm_Cuenta;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
        
        
/**
 *
 * @author avila
 */
public class Controlador_Cuentas_Contables implements ActionListener{

    private final Menu_Sistema menu;
     private  Frm_Cuenta Cuenta_Contables ;
     
     
     public  Controlador_Cuentas_Contables (Menu_Sistema menu){
         this.menu= menu;
     }
    
     
     
     public  void MostrarVista (){
         if (Cuenta_Contables == null) {
             
             Cuenta_Contables = new Frm_Cuenta();
             
             
             // AgregarListener
             AgregarListeners();
             
             
         }
         
         
         Cuenta_Contables.setVisible(true);

CargarCuentasd();
menu.setVisible(false);
         
         
     }
    
     
     private  void AgregarListeners (){
         
         Cuenta_Contables.btbVolverMenu.addActionListener(this);
     }
    
    
     
     
     
    @Override
    public void actionPerformed(ActionEvent e) {
       
        
        if (e.getSource() == Cuenta_Contables.btbVolverMenu) {
          VolverMenu();
        }
    }
    
    
    private  void VolverMenu (){
        menu.setVisible(true);
        
        Cuenta_Contables.setVisible(false);
    }
    
    
    private  void CargarCuentasd (){
        ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
        try {
            cuentaDao.MostrarListaDeCuenta();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
