/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.udb.www.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sv.edu.udb.www.beans.oferta;
import sv.edu.udb.www.model.OfertaModel;
import sv.edu.udb.www.utils.Validaciones;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.io.FileNotFoundException;

/**
 *
 * @author Lbeats
 */
@WebServlet(name = "OfertaController", urlPatterns = {"/oferta.do"})
public class OfertaController extends HttpServlet {

    OfertaModel Model = new OfertaModel();
    
    ArrayList listaErrores = new ArrayList();
    
    
    protected void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         try {
            request.setAttribute("listarofertas", Model.listarofertas());
            request.getRequestDispatcher("/admin/table.jsp").forward(request, response);
        } catch (SQLException | ServletException | IOException ex) {
          Logger.getLogger(OfertaController.class.getName()).log(Level.SEVERE, null, ex);
    }   
    }
    
    protected void insertar(MultipartRequest multi, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        
        try {
            oferta mioferta = new oferta();
            mioferta.setTitulo(multi.getParameter("titulo"));
            mioferta.setPrecio1((Double.parseDouble(multi.getParameter("precio1"))));
            mioferta.setPrecio2((Double.parseDouble(multi.getParameter("precio2"))));
            mioferta.setFecha1(multi.getParameter("fecha1"));
            mioferta.setFecha2(multi.getParameter("fecha2"));
            mioferta.setDescri(multi.getParameter("descrip"));
         
            if (multi.getFile("archivo") == null  ){
                listaErrores.add("La imagen es obligatoria");
            }else{
                
                File ficheroTemp = multi.getFile("archivo");
                
                mioferta.setUrlfoto(ficheroTemp.getName());
                if (Validaciones.isEmpty(mioferta.getTitulo())){
                    listaErrores.add("El titulo de la oferta es obligatorio");
                    multi.getFile("archivo").delete();
                }
                
                if(Validaciones.isEmpty(mioferta.getFecha1())){
                    listaErrores.add("La fecha es obligatorio");
                    multi.getFile("archivo").delete();
                }
                
                if(listaErrores.isEmpty()){
                    Model.insertarOferta(mioferta);
                    response.sendRedirect(request.getContextPath() + "/oferta.do?operacion=listar");
                }else{
                    request.setAttribute("listaErrores", listaErrores);
                    request.setAttribute("empleado", mioferta);
                    request.getRequestDispatcher("admin/user.jsp").forward(request, response);
          
                }
            }
            
        } catch (IOException | SQLException | ServletException ex) {
          Logger.getLogger(OfertaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
            }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listaErrores.clear();
        String operacion = request.getParameter("operacion");
        switch (operacion){
            case "nuevo":
                request.getRequestDispatcher("/admin/user.jsp").forward(request, response);
                break;
            case "listar":
                listar(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listaErrores.clear();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String directorio = getServletContext().getRealPath("/img");
            MultipartRequest multi = new MultipartRequest(request, directorio, 1 * 1024 * 1024, new DefaultFileRenamePolicy());
            String operacion = multi.getParameter("operacion");
            if (operacion.equals("insertar")){
                insertar(multi, request, response);
            }
        } catch (FileNotFoundException e) {
            out.println("La ruta de ubicacion no existe");
            
        } catch (IOException e) {
            out.println("El archivo seleccionado supera el maximo de 1MB");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
