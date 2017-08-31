/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.udb.www.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sv.edu.udb.www.beans.oferta;
import sv.edu.udb.www.model.Conexion;
import static sv.edu.udb.www.model.Conexion.conexion;



public class OfertaModel extends Conexion {
     public int insertarOferta(oferta emp) throws SQLException {
                try {
                    int filasAfectadas=0;
                sql = "INSERT INTO oferta VALUES(null,?,?,?,null,null,?,?,?,null,?)";
                        this.conectar();
                st = conexion.prepareStatement(sql);
                st.setString(1, emp.getTitulo());
                st.setDouble(2, emp.getPrecio1());
                st.setDouble(3, emp.getPrecio2());
                st.setString(4, emp.getFecha1());
                st.setString(5, emp.getFecha2());          
                st.setString(6, emp.getDescri());
                st.setString(7, emp.getUrlfoto());
                    filasAfectadas= st.executeUpdate();
                    this.desconectar();
                    return filasAfectadas;
                } catch (SQLException ex) {
                    Logger.getLogger(OfertaModel.class.getName()).log(Level.SEVERE,null, ex);
 this.desconectar();
 return 0;
 }
 }



public List<oferta> listarofertas() throws SQLException {
 List<oferta> listarofertas = new ArrayList<>();
        try {
            this.conectar();
                sql = "SELECT * FROM oferta";
                st = conexion.prepareStatement(sql);
                    rs = st.executeQuery();
                while (rs.next()) {
            oferta mioferta = new oferta();
            mioferta.setTitulo(rs.getString("Titulo"));
            mioferta.setFecha1(rs.getString("Fecha_inicio"));
            mioferta.setFecha2(rs.getString("Fecha_final"));
            mioferta.setPrecio1(rs.getDouble("Precio_regular"));
            mioferta.setPrecio2(rs.getDouble("Precio_oferta"));
            mioferta.setDescri(rs.getString("estado"));
            mioferta.setUrlfoto(rs.getString("url_Foto"));
            listarofertas.add(mioferta);
 }
 } catch (SQLException ex) {
 Logger.getLogger(OfertaModel.class.getName()).log(Level.SEVERE,null, ex);
 }
 this.desconectar();
 return listarofertas;
 }
}
