/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.eb.ime.sisop.multilevelfeedback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author arthurfernandes
 */
@WebServlet(name = "DefaultController", urlPatterns = {"/ganttchart.html"})
public class DefaultController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        List<Processo> listaDeProcessos = new ArrayList<Processo>();
        
        for(int i = 0;i<5;i++){
            String nome = null;
            String surto = null;
            String io = null;
            
            nome = request.getParameter("nome"+(i+1));
            surto = request.getParameter("burst"+(i+1));
            io = request.getParameter("io"+(i+1));
            
            try{
                Processo process = new Processo(Integer.parseInt(io),Integer.parseInt(surto),nome);
                listaDeProcessos.add(process);
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        EscalonMultFilas escalonador = new EscalonMultFilas(listaDeProcessos);
        
        escalonador.escalonar();
        
        List<EscalonMultFilas.NoCPU> listaUsoCPU = escalonador.getUsoCPU();
        List<EscalonMultFilas.NoIO> listaUsoIO = escalonador.getUsoIO();
        
        request.setAttribute("listaUsoCPU", listaUsoCPU);
        request.setAttribute("listaUsoIO", listaUsoIO);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/ganttchart.jsp");
        requestDispatcher.forward(request, response);
        
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
        processRequest(request, response);
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
        processRequest(request, response);
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
